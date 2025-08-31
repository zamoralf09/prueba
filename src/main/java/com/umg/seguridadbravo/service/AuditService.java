package com.umg.seguridadbravo.service;

import com.umg.seguridadbravo.entity.BitacoraAcceso;
import com.umg.seguridadbravo.entity.TipoAcceso;
import com.umg.seguridadbravo.repository.BitacoraAccesoRepository;
import com.umg.seguridadbravo.repository.TipoAccesoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AuditService {

    @Autowired
    private BitacoraAccesoRepository bitacoraAccesoRepository;

    @Autowired
    private TipoAccesoRepository tipoAccesoRepository;

    public List<BitacoraAcceso> findRecentActivity(LocalDateTime since, int limit) {
        return bitacoraAccesoRepository.findByFechaAccesoAfterOrderByFechaAccesoDesc(since, PageRequest.of(0, limit))
                .getContent();
    }

    public List<BitacoraAcceso> getAccessLogsByUser(String username) {
        return bitacoraAccesoRepository.findByIdUsuarioOrderByFechaAccesoDesc(username);
    }

    public List<BitacoraAcceso> getAccessLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return bitacoraAccesoRepository.findByFechaAccesoBetween(startDate, endDate);
    }

    public List<BitacoraAcceso> getAccessLogsByUserAndDateRange(String username,
            LocalDateTime startDate,
            LocalDateTime endDate) {
        return bitacoraAccesoRepository.findByUsuarioAndFechaBetween(username, startDate, endDate);
    }

    public long getSuccessfulLoginsCount(LocalDateTime since) {
        return bitacoraAccesoRepository.countAccesosConcedidosDesde(since);
    }

    public long getFailedLoginsCount(LocalDateTime since) {
        return bitacoraAccesoRepository.countAccesosBloqueadosDesde(since);
    }

    public Map<String, Long> getLoginStatsByType(LocalDateTime since) {
        List<BitacoraAcceso> logs = bitacoraAccesoRepository.findByFechaAccesoBetween(since, LocalDateTime.now());

        return logs.stream()
                .collect(Collectors.groupingBy(
                        log -> log.getTipoAcceso().getNombre(),
                        Collectors.counting()));
    }

    public Map<String, Long> getLoginStatsByUser(LocalDateTime since) {
        List<BitacoraAcceso> logs = bitacoraAccesoRepository.findByFechaAccesoBetween(since, LocalDateTime.now());

        return logs.stream()
                .collect(Collectors.groupingBy(
                        BitacoraAcceso::getIdUsuario,
                        Collectors.counting()));
    }

    public Map<String, Long> getLoginStatsByBrowser(LocalDateTime since) {
        List<BitacoraAcceso> logs = bitacoraAccesoRepository.findByFechaAccesoBetween(since, LocalDateTime.now());

        return logs.stream()
                .filter(log -> log.getBrowser() != null)
                .collect(Collectors.groupingBy(
                        BitacoraAcceso::getBrowser,
                        Collectors.counting()));
    }

    public Map<String, Long> getLoginStatsByOS(LocalDateTime since) {
        List<BitacoraAcceso> logs = bitacoraAccesoRepository.findByFechaAccesoBetween(since, LocalDateTime.now());

        return logs.stream()
                .filter(log -> log.getSistemaOperativo() != null)
                .collect(Collectors.groupingBy(
                        BitacoraAcceso::getSistemaOperativo,
                        Collectors.counting()));
    }

    public List<String> getMostActiveUsers(LocalDateTime since, int limit) {
        List<BitacoraAcceso> logs = bitacoraAccesoRepository.findByFechaAccesoBetween(since, LocalDateTime.now());

        return logs.stream()
                .collect(Collectors.groupingBy(
                        BitacoraAcceso::getIdUsuario,
                        Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<String> getMostFailedLoginUsers(LocalDateTime since, int limit) {
        List<BitacoraAcceso> logs = bitacoraAccesoRepository.findByFechaAccesoBetween(since, LocalDateTime.now());

        return logs.stream()
                .filter(log -> log.getTipoAcceso().getNombre().contains("Bloqueado") ||
                        log.getTipoAcceso().getNombre().contains("incorrecto"))
                .collect(Collectors.groupingBy(
                        BitacoraAcceso::getIdUsuario,
                        Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public Page<BitacoraAcceso> getAccessLogsPageable(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return bitacoraAccesoRepository.findAll(pageable);
    }

    public SecurityReport generateSecurityReport(LocalDateTime startDate, LocalDateTime endDate) {
        SecurityReport report = new SecurityReport();
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setTotalLogins(getSuccessfulLoginsCount(startDate));
        report.setFailedLogins(getFailedLoginsCount(startDate));
        report.setLoginsByType(getLoginStatsByType(startDate));
        report.setLoginsByUser(getLoginStatsByUser(startDate));
        report.setLoginsByBrowser(getLoginStatsByBrowser(startDate));
        report.setLoginsByOS(getLoginStatsByOS(startDate));
        report.setMostActiveUsers(getMostActiveUsers(startDate, 10));
        report.setMostFailedLoginUsers(getMostFailedLoginUsers(startDate, 10));

        return report;
    }

    public List<String> getSystemAlerts() {
        // Ejemplo de alertas generadas dinámicamente
        List<String> alerts = new ArrayList<>();

        // Verificar si hay muchos intentos fallidos de inicio de sesión en las últimas
        // 24 horas
        long failedLogins = getFailedLoginsCount(LocalDateTime.now().minusDays(1));
        if (failedLogins > 50) {
            alerts.add(
                    "Alerta: Se detectaron más de 50 intentos fallidos de inicio de sesión en las últimas 24 horas.");
        }

        // Verificar si hay usuarios bloqueados recientemente
        long blockedUsers = bitacoraAccesoRepository.countUsuariosBloqueadosRecientes(LocalDateTime.now().minusDays(1));
        if (blockedUsers > 0) {
            alerts.add("Alerta: " + blockedUsers + " usuarios fueron bloqueados en las últimas 24 horas.");
        }

        return alerts;
    }

    public static class SecurityReport {
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private long totalLogins;
        private long failedLogins;
        private Map<String, Long> loginsByType;
        private Map<String, Long> loginsByUser;
        private Map<String, Long> loginsByBrowser;
        private Map<String, Long> loginsByOS;
        private List<String> mostActiveUsers;
        private List<String> mostFailedLoginUsers;

        // Getters and Setters
        public LocalDateTime getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDateTime startDate) {
            this.startDate = startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDateTime endDate) {
            this.endDate = endDate;
        }

        public long getTotalLogins() {
            return totalLogins;
        }

        public void setTotalLogins(long totalLogins) {
            this.totalLogins = totalLogins;
        }

        public long getFailedLogins() {
            return failedLogins;
        }

        public void setFailedLogins(long failedLogins) {
            this.failedLogins = failedLogins;
        }

        public Map<String, Long> getLoginsByType() {
            return loginsByType;
        }

        public void setLoginsByType(Map<String, Long> loginsByType) {
            this.loginsByType = loginsByType;
        }

        public Map<String, Long> getLoginsByUser() {
            return loginsByUser;
        }

        public void setLoginsByUser(Map<String, Long> loginsByUser) {
            this.loginsByUser = loginsByUser;
        }

        public Map<String, Long> getLoginsByBrowser() {
            return loginsByBrowser;
        }

        public void setLoginsByBrowser(Map<String, Long> loginsByBrowser) {
            this.loginsByBrowser = loginsByBrowser;
        }

        public Map<String, Long> getLoginsByOS() {
            return loginsByOS;
        }

        public void setLoginsByOS(Map<String, Long> loginsByOS) {
            this.loginsByOS = loginsByOS;
        }

        public List<String> getMostActiveUsers() {
            return mostActiveUsers;
        }

        public void setMostActiveUsers(List<String> mostActiveUsers) {
            this.mostActiveUsers = mostActiveUsers;
        }

        public List<String> getMostFailedLoginUsers() {
            return mostFailedLoginUsers;
        }

        public void setMostFailedLoginUsers(List<String> mostFailedLoginUsers) {
            this.mostFailedLoginUsers = mostFailedLoginUsers;
        }
    }
}
