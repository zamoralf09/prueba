package com.umg.seguridadbravo.service;

import com.umg.seguridadbravo.entity.Usuario;
import com.umg.seguridadbravo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Transactional
public class SessionService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthenticationService authenticationService;

    private final ConcurrentMap<String, SessionInfo> activeSessions = new ConcurrentHashMap<>();

    public void registerSession(String username, String sessionId, HttpSession session) {
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setUsername(username);
        sessionInfo.setSessionId(sessionId);
        sessionInfo.setCreationTime(LocalDateTime.now());
        sessionInfo.setLastAccessTime(LocalDateTime.now());
        sessionInfo.setHttpSession(session);

        activeSessions.put(sessionId, sessionInfo);

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setSesionActual(sessionId);
            usuarioRepository.save(usuario);
        }
    }

    public void updateSessionActivity(String sessionId) {
        SessionInfo sessionInfo = activeSessions.get(sessionId);
        if (sessionInfo != null) {
            sessionInfo.setLastAccessTime(LocalDateTime.now());
        }
    }

    public void invalidateSession(String sessionId) {
        SessionInfo sessionInfo = activeSessions.remove(sessionId);
        if (sessionInfo != null) {
            // Clear user's current session
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(sessionInfo.getUsername());
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                usuario.setSesionActual(null);
                usuarioRepository.save(usuario);
            }

            // Invalidate HTTP session
            if (sessionInfo.getHttpSession() != null) {
                try {
                    sessionInfo.getHttpSession().invalidate();
                } catch (IllegalStateException e) {
                    // Session already invalidated
                }
            }
        }
    }

    public void invalidateUserSessions(String username) {
        activeSessions.entrySet().removeIf(entry -> {
            SessionInfo sessionInfo = entry.getValue();
            if (username.equals(sessionInfo.getUsername())) {
                if (sessionInfo.getHttpSession() != null) {
                    try {
                        sessionInfo.getHttpSession().invalidate();
                    } catch (IllegalStateException e) {
                    }
                }
                return true;
            }
            return false;
        });

        // limpiar la sesión actual del usuario en la base de datos
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setSesionActual(null);
            usuarioRepository.save(usuario);
        }
    }

    public boolean isSessionActive(String sessionId) {
        return activeSessions.containsKey(sessionId);
    }

    public SessionInfo getSessionInfo(String sessionId) {
        return activeSessions.get(sessionId);
    }

    public List<SessionInfo> getActiveSessionsForUser(String username) {
        return activeSessions.values().stream()
                .filter(session -> username.equals(session.getUsername()))
                .toList();
    }

    public int getActiveSessionCount() {
        return activeSessions.size();
    }

    public int countActiveSessions() {
        return getActiveSessionCount();
    }

    // elimina sesiones expiradas basadas en inactividad
    public void cleanupExpiredSessions(int maxInactiveMinutes) {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(maxInactiveMinutes);

        activeSessions.entrySet().removeIf(entry -> {
            SessionInfo sessionInfo = entry.getValue();
            if (sessionInfo.getLastAccessTime().isBefore(cutoffTime)) {

                authenticationService.logUserActivity(
                        sessionInfo.getUsername(),
                        "SESSION_EXPIRED",
                        null);

                if (sessionInfo.getHttpSession() != null) {
                    try {
                        sessionInfo.getHttpSession().invalidate();
                    } catch (IllegalStateException e) {
                        // Session already invalidated
                    }
                }

                return true;
            }
            return false;
        });
    }

    public static class SessionInfo {
        private String username;
        private String sessionId;
        private LocalDateTime creationTime;
        private LocalDateTime lastAccessTime;
        private HttpSession httpSession;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public LocalDateTime getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(LocalDateTime creationTime) {
            this.creationTime = creationTime;
        }

        public LocalDateTime getLastAccessTime() {
            return lastAccessTime;
        }

        public void setLastAccessTime(LocalDateTime lastAccessTime) {
            this.lastAccessTime = lastAccessTime;
        }

        public HttpSession getHttpSession() {
            return httpSession;
        }

        public void setHttpSession(HttpSession httpSession) {
            this.httpSession = httpSession;
        }
    }
}
