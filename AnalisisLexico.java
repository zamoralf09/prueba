/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.umg.lexico;

import java.util.LinkedList;

/**
 *
 *
 */
public class AnalisisLexico {

    LinkedList<Simbolo> tablaLexico = new LinkedList<>();
    LinkedList<String> errorLexico = new LinkedList<>();

    public String anlizar(String entrada) {
        String codigoHtml = "";
        Simbolo s = new Simbolo();
        char[] letras = entrada.toCharArray();
        char estado = 'A';
        int columna = 0;
        int linea = 1;
        boolean validarCaracteres;
        boolean parComentarios = false;

        for (char letra : letras) {
            columna++;
            validarCaracteres = false;
            if (parComentarios) {
                if (letra == '\n') {
                    parComentarios = false;
                    codigoHtml += "<br>";
                    columna = 0;
                    linea++;
                } else {
                    codigoHtml += "<font size='12' color='gray'>" + letra + "</font>";
                }
                continue;
            }
            switch (estado) {
                case 'A':
                    switch (letra) {
                        case 'i':
                            estado = 'B';
                            s.lexema = "i";
                            s.columna = columna;
                            s.linea = linea;
                            break;
                        case 'e':
                            estado = 'C';
                            s.lexema = "e";
                            s.columna = columna;
                            s.linea = linea;
                            break;
                        case 'v':
                            estado = 'D';
                            s.lexema = "v";
                            s.columna = columna;
                            s.linea = linea;
                            break;
                        case '+':
                            codigoHtml += "<font size='12' color='yellow'>+</font>";
                            s.lexema = "+";
                            s.expresionRegular = "+";
                            s.token = "Mas";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '-':
                            codigoHtml += "<font size='12' color='yellow'>-</font>";
                            s.lexema = "-";
                            s.expresionRegular = "-";
                            s.token = "Menos";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '*':
                            codigoHtml += "<font size='12' color='yellow'>*</font>";
                            s.lexema = "*";
                            s.expresionRegular = "*";
                            s.token = "Por";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '/':
                            codigoHtml += "<font size='12' color='yellow'>/</font>";
                            s.lexema = "/";
                            s.expresionRegular = "/";
                            s.token = "Division";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;

                        case '<':
                            codigoHtml += "<font size='12' color='yellow'>&lt;</font>";
                            s.lexema = "<";
                            s.expresionRegular = "<";
                            s.token = "Menor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '>':
                            codigoHtml += "<font size='12' color='yellow'>&gt;</font>";
                            s.lexema = ">";
                            s.expresionRegular = ">";
                            s.token = "Mayor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '=':
                            codigoHtml += "<font size='12' color='aqua'>=</font>";
                            s.lexema = "=";
                            s.expresionRegular = "=";
                            s.token = "Igual";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '{':
                            codigoHtml += "<font size='12' color='purple'>{</font>";
                            s.lexema = "{";
                            s.expresionRegular = "{";
                            s.token = "Llave abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '}':
                            codigoHtml += "<font size='12' color='purple'>}</font>";
                            s.lexema = "}";
                            s.expresionRegular = "}";
                            s.token = "Llave cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '[':
                            codigoHtml += "<font size='12' color='purple'>[</font>";
                            s.lexema = "[";
                            s.expresionRegular = "[";
                            s.token = "Apertura Braket";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ']':
                            codigoHtml += "<font size='12' color='purple'>]</font>";
                            s.lexema = "]";
                            s.expresionRegular = "]";
                            s.token = "Cerrar Braket";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '(':
                            codigoHtml += "<font size='12' color='purple'>(</font>";
                            s.lexema = "(";
                            s.expresionRegular = "(";
                            s.token = "par. abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ')':
                            codigoHtml += "<font size='12' color='purple'>)</font>";
                            s.lexema = ")";
                            s.expresionRegular = ")";
                            s.token = "par. cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ':':
                            codigoHtml += "<font size='12' color='green'>:</font>";
                            s.lexema = ":";
                            s.expresionRegular = ":";
                            s.token = "Dos Puntos";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ';':
                            codigoHtml += "<font size='12' color='green' >;</font>";
                            s.lexema = ";";
                            s.expresionRegular = ";";
                            s.token = "punto y coma";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '!':
                            codigoHtml += "<font size='12' color='red' >!</font>";
                            s.lexema = "!";
                            s.expresionRegular = "!";
                            s.token = "Operador Logico";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '&':
                            codigoHtml += "<font size='12' color='red' >&</font>";
                            s.lexema = "&";
                            s.expresionRegular = "&";
                            s.token = "Operador Logico";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            columna++;
                            break;
                        case '|':
                            codigoHtml += "<font size='12' color='red' >|</font>";
                            s.lexema = "|";
                            s.expresionRegular = "|";
                            s.token = "Operador Logico";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;

                        case '%':
                            codigoHtml += "<font size='12' color='red' >%</font>";
                            s.lexema = "%";
                            s.expresionRegular = "%";
                            s.token = "Operador Logico";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '^':
                            codigoHtml += "<font size='12' color='red' >^</font>";
                            s.lexema = "^";
                            s.expresionRegular = "^";
                            s.token = "Operador Logico";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '\'':
                            parComentarios = true;
                            codigoHtml += "<font size='12' color='gray'>'</font>";
                        case ' ':
                            codigoHtml += "&nbsp;";
                            break;
                        case '\n':
                            columna = 0;
                            linea++;
                            codigoHtml += "<br>";
                            break;
                        case '\t':
                            codigoHtml += "&nbsp;&nbsp;&nbsp;";
                            break;
                        default:
                            validarCaracteres = true;
                    }
                    if (validarCaracteres) {
                        if ((letra >= 65 && letra <= 90) || (letra >= 97 && letra <= 122)) {
                            estado = 'E';
                            s.lexema = letra + "";
                            s.columna = columna;
                            s.linea = linea;
                        } else {
                            if (letra >= 48 && letra <= 57) {
                                estado = 'F';
                                s.lexema = letra + "";
                                s.columna = columna;
                                s.linea = linea;
                            } else {
                                String error = "Error lexico linea " + linea + ", columna: " + columna + ", caracter no reconocido: " + letra;
                                errorLexico.add(error);
                            }
                        }

                    }
                    break;
                case 'B':
                    switch (letra) {
                        case 'f':
                            estado = 'G';
                            s.lexema = "if";
                            break;
                        case '_':
                            estado = 'E';
                            s.lexema += "_";
                            break;
                        case '+':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>+</font>";
                            s.lexema = "+";
                            s.expresionRegular = "+";
                            s.token = "mas";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '-':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>-</font>";
                            s.lexema = "-";
                            s.expresionRegular = "-";
                            s.token = "menos";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '*':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>*</font>";
                            s.lexema = "*";
                            s.expresionRegular = "*";
                            s.token = "Por";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '<':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&lt;</font>";
                            s.lexema = "<";
                            s.expresionRegular = "<";
                            s.token = "menor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '>':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&gt;</font>";
                            s.lexema = ">";
                            s.expresionRegular = ">";
                            s.token = "mayor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '=':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='aqua'>=</font>";
                            s.lexema = "=";
                            s.expresionRegular = "=";
                            s.token = "igual";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '{':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>{</font>";
                            s.lexema = "{";
                            s.expresionRegular = "{";
                            s.token = "llave abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '}':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>}</font>";
                            s.lexema = "}";
                            s.expresionRegular = "}";
                            s.token = "llave cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '(':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>(</font>";
                            s.lexema = "(";
                            s.expresionRegular = "(";
                            s.token = "par. abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ')':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>)</font>";
                            s.lexema = ")";
                            s.expresionRegular = ")";
                            s.token = "par. cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ';':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='green'>;</font>";
                            s.lexema = ";";
                            s.expresionRegular = ";";
                            s.token = "punto y coma";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ' ':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;";
                            break;
                        case '\n':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            columna = 0;
                            linea++;
                            codigoHtml += "<br>";
                            break;
                        case '\t':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;&nbsp;&nbsp;";
                            break;
                        default:
                            validarCaracteres = true;
                    }
                    if (validarCaracteres) {
                        if ((letra >= 65 && letra <= 90) || (letra >= 97 && letra <= 122)) {
                            estado = 'E';
                            s.lexema += letra;
                            s.columna = columna;
                            s.linea = linea;
                        } else {
                            if (letra >= 48 && letra <= 57) {
                                estado = 'E';
                                s.lexema += letra;
                                s.columna = columna;
                                s.linea = linea;
                            } else {
                                estado = 'A';
                                s.token = "identificador";
                                s.expresionRegular = "L(L|D|_)*";
                                codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                                tablaLexico.add(s);
                                s = new Simbolo();
                                String error = "Error lexico linea " + linea + ", columna: " + columna + ", caracter no reconocido: " + letra;
                                errorLexico.add(error);
                            }
                        }

                    }
                    break;
                case 'G':
                    switch (letra) {
                        case '_':
                            estado = 'E';
                            s.lexema += "_";
                            break;
                        case '+':
                            estado = 'A';
                            s.token = "if";
                            s.expresionRegular = "if";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>+</font>";
                            s.lexema = "+";
                            s.expresionRegular = "+";
                            s.token = "mas";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '<':
                            estado = 'A';
                            s.token = "if";
                            s.expresionRegular = "if";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&lt;</font>";
                            s.lexema = "<";
                            s.expresionRegular = "<";
                            s.token = "menor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '>':
                            estado = 'A';
                            s.token = "if";
                            s.expresionRegular = "if";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&gt;</font>";
                            s.lexema = ">";
                            s.expresionRegular = ">";
                            s.token = "mayor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '=':
                            estado = 'A';
                            s.token = "if";
                            s.expresionRegular = "if";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='aqua'>=</font>";
                            s.lexema = "=";
                            s.expresionRegular = "=";
                            s.token = "igual";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '{':
                            estado = 'A';
                            s.token = "if";
                            s.expresionRegular = "if";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>{</font>";
                            s.lexema = "{";
                            s.expresionRegular = "{";
                            s.token = "llave abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '}':
                            estado = 'A';
                            s.token = "if";
                            s.expresionRegular = "if";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>}</font>";
                            s.lexema = "}";
                            s.expresionRegular = "}";
                            s.token = "llave cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '(':
                            estado = 'A';
                            s.token = "if";
                            s.expresionRegular = "if";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>(</font>";
                            s.lexema = "(";
                            s.expresionRegular = "(";
                            s.token = "par. abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ')':
                            estado = 'A';
                            s.token = "if";
                            s.expresionRegular = "if";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>)</font>";
                            s.lexema = ")";
                            s.expresionRegular = ")";
                            s.token = "par. cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ';':
                            estado = 'A';
                            s.token = "if";
                            s.expresionRegular = "if";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='green'>;</font>";
                            s.lexema = ";";
                            s.expresionRegular = ";";
                            s.token = "punto y coma";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ' ':
                            estado = 'A';
                            s.token = "if";
                            s.expresionRegular = "if";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;";
                            break;
                        case '\n':
                            estado = 'A';
                            s.token = "if";
                            s.expresionRegular = "if";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            columna = 0;
                            linea++;
                            codigoHtml += "<br>";
                            break;
                        case '\t':
                            estado = 'A';
                            s.token = "if";
                            s.expresionRegular = "if";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;&nbsp;&nbsp;";
                            break;
                        default:
                            validarCaracteres = true;
                    }
                    if (validarCaracteres) {
                        if ((letra >= 65 && letra <= 90) || (letra >= 97 && letra <= 122)) {
                            estado = 'E';
                            s.lexema += letra;
                            s.columna = columna;
                            s.linea = linea;
                        } else {
                            if (letra >= 48 && letra <= 57) {
                                estado = 'E';
                                s.lexema += letra;
                                s.columna = columna;
                                s.linea = linea;
                            } else {
                                estado = 'A';
                                s.token = "if";
                                s.expresionRegular = "if";
                                codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                                tablaLexico.add(s);
                                s = new Simbolo();
                                String error = "Error lexico linea " + linea + ", columna: " + columna + ", caracter no reconocido: " + letra;
                                errorLexico.add(error);
                            }
                        }

                    }
                    break;
                case 'C':
                    switch (letra) {
                        case 'l':
                            estado = 'H';
                            s.lexema = "el";
                            break;
                        case '_':
                            estado = 'E';
                            s.lexema += "_";
                            break;
                        case '+':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>+</font>";
                            s.lexema = "+";
                            s.expresionRegular = "+";
                            s.token = "mas";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '-':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>-</font>";
                            s.lexema = "-";
                            s.expresionRegular = "-";
                            s.token = "menos";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '*':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>*</font>";
                            s.lexema = "*";
                            s.expresionRegular = "*";
                            s.token = "Por";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '<':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&lt;</font>";
                            s.lexema = "<";
                            s.expresionRegular = "<";
                            s.token = "menor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '>':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&gt;</font>";
                            s.lexema = ">";
                            s.expresionRegular = ">";
                            s.token = "mayor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '=':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='aqua'>=</font>";
                            s.lexema = "=";
                            s.expresionRegular = "=";
                            s.token = "igual";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '{':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>{</font>";
                            s.lexema = "{";
                            s.expresionRegular = "{";
                            s.token = "llave abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '}':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>}</font>";
                            s.lexema = "}";
                            s.expresionRegular = "}";
                            s.token = "llave cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '(':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>(</font>";
                            s.lexema = "(";
                            s.expresionRegular = "(";
                            s.token = "par. abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ')':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>)</font>";
                            s.lexema = ")";
                            s.expresionRegular = ")";
                            s.token = "par. cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ';':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='green'>;</font>";
                            s.lexema = ";";
                            s.expresionRegular = ";";
                            s.token = "punto y coma";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ' ':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;";
                            break;
                        case '\n':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            columna = 0;
                            linea++;
                            codigoHtml += "<br>";
                            break;
                        case '\t':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;&nbsp;&nbsp;";
                            break;
                        default:
                            validarCaracteres = true;
                    }
                    if (validarCaracteres) {
                        if ((letra >= 65 && letra <= 90) || (letra >= 97 && letra <= 122)) {
                            estado = 'E';
                            s.lexema += letra;
                            s.columna = columna;
                            s.linea = linea;
                        } else {
                            if (letra >= 48 && letra <= 57) {
                                estado = 'E';
                                s.lexema += letra;
                                s.columna = columna;
                                s.linea = linea;
                            } else {
                                estado = 'A';
                                s.token = "identificador";
                                s.expresionRegular = "L(L|D|_)*";
                                codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                                tablaLexico.add(s);
                                s = new Simbolo();
                                String error = "Error lexico linea " + linea + ", columna: " + columna + ", caracter no reconocido: " + letra;
                                errorLexico.add(error);
                            }
                        }

                    }
                    break;
                case 'H':
                    switch (letra) {
                        case 's':
                            estado = 'I';
                            s.lexema = "els";
                            break;
                        case '_':
                            estado = 'E';
                            s.lexema += "_";
                            break;
                        case '+':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>+</font>";
                            s.lexema = "+";
                            s.expresionRegular = "+";
                            s.token = "mas";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '-':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>-</font>";
                            s.lexema = "-";
                            s.expresionRegular = "-";
                            s.token = "menos";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '<':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&lt;</font>";
                            s.lexema = "<";
                            s.expresionRegular = "<";
                            s.token = "menor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '>':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&gt;</font>";
                            s.lexema = ">";
                            s.expresionRegular = ">";
                            s.token = "mayor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '=':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='aqua'>=</font>";
                            s.lexema = "=";
                            s.expresionRegular = "=";
                            s.token = "igual";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '{':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>{</font>";
                            s.lexema = "{";
                            s.expresionRegular = "{";
                            s.token = "llave abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '}':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>}</font>";
                            s.lexema = "}";
                            s.expresionRegular = "}";
                            s.token = "llave cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '(':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>(</font>";
                            s.lexema = "(";
                            s.expresionRegular = "(";
                            s.token = "par. abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ')':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>)</font>";
                            s.lexema = ")";
                            s.expresionRegular = ")";
                            s.token = "par. cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ';':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='green'>;</font>";
                            s.lexema = ";";
                            s.expresionRegular = ";";
                            s.token = "punto y coma";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ' ':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;";
                            break;
                        case '\n':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            columna = 0;
                            linea++;
                            codigoHtml += "<br>";
                            break;
                        case '\t':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;&nbsp;&nbsp;";
                            break;
                        default:
                            validarCaracteres = true;
                    }
                    if (validarCaracteres) {
                        if ((letra >= 65 && letra <= 90) || (letra >= 97 && letra <= 122)) {
                            estado = 'E';
                            s.lexema += letra;
                            s.columna = columna;
                            s.linea = linea;
                        } else {
                            if (letra >= 48 && letra <= 57) {
                                estado = 'E';
                                s.lexema += letra;
                                s.columna = columna;
                                s.linea = linea;
                            } else {
                                estado = 'A';
                                s.token = "identificador";
                                s.expresionRegular = "L(L|D|_)*";
                                codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                                tablaLexico.add(s);
                                s = new Simbolo();
                                String error = "Error lexico linea " + linea + ", columna: " + columna + ", caracter no reconocido: " + letra;
                                errorLexico.add(error);
                            }
                        }

                    }
                    break;
                case 'I':
                    switch (letra) {
                        case 'e':
                            estado = 'J';
                            s.lexema = "else";
                            break;
                        case '_':
                            estado = 'E';
                            s.lexema += "_";
                            break;
                        case '+':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>+</font>";
                            s.lexema = "+";
                            s.expresionRegular = "+";
                            s.token = "mas";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '-':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>-</font>";
                            s.lexema = "-";
                            s.expresionRegular = "-";
                            s.token = "menos";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '<':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&lt;</font>";
                            s.lexema = "<";
                            s.expresionRegular = "<";
                            s.token = "menor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '>':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&gt;</font>";
                            s.lexema = ">";
                            s.expresionRegular = ">";
                            s.token = "mayor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '=':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='aqua'>=</font>";
                            s.lexema = "=";
                            s.expresionRegular = "=";
                            s.token = "igual";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '{':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>{</font>";
                            s.lexema = "{";
                            s.expresionRegular = "{";
                            s.token = "llave abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '}':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>}</font>";
                            s.lexema = "}";
                            s.expresionRegular = "}";
                            s.token = "llave cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '(':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>(</font>";
                            s.lexema = "(";
                            s.expresionRegular = "(";
                            s.token = "par. abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ')':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>)</font>";
                            s.lexema = ")";
                            s.expresionRegular = ")";
                            s.token = "par. cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ';':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='green'>;</font>";
                            s.lexema = ";";
                            s.expresionRegular = ";";
                            s.token = "punto y coma";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ' ':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;";
                            break;
                        case '\n':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            columna = 0;
                            linea++;
                            codigoHtml += "<br>";
                            break;
                        case '\t':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;&nbsp;&nbsp;";
                            break;
                        default:
                            validarCaracteres = true;
                    }
                    if (validarCaracteres) {
                        if ((letra >= 65 && letra <= 90) || (letra >= 97 && letra <= 122)) {
                            estado = 'E';
                            s.lexema += letra;
                            s.columna = columna;
                            s.linea = linea;
                        } else {
                            if (letra >= 48 && letra <= 57) {
                                estado = 'E';
                                s.lexema += letra;
                                s.columna = columna;
                                s.linea = linea;
                            } else {
                                estado = 'A';
                                s.token = "identificador";
                                s.expresionRegular = "L(L|D|_)*";
                                codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                                tablaLexico.add(s);
                                s = new Simbolo();
                                String error = "Error lexico linea " + linea + ", columna: " + columna + ", caracter no reconocido: " + letra;
                                errorLexico.add(error);
                            }
                        }

                    }
                    break;
                case 'J':
                    switch (letra) {
                        case '_':
                            estado = 'E';
                            s.lexema += "_";
                            break;
                        case '+':
                            estado = 'A';
                            s.token = "else";
                            s.expresionRegular = "else";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>+</font>";
                            s.lexema = "+";
                            s.expresionRegular = "+";
                            s.token = "mas";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '<':
                            estado = 'A';
                            s.token = "else";
                            s.expresionRegular = "else";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&lt;</font>";
                            s.lexema = "<";
                            s.expresionRegular = "<";
                            s.token = "menor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '>':
                            estado = 'A';
                            s.token = "else";
                            s.expresionRegular = "else";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&gt;</font>";
                            s.lexema = ">";
                            s.expresionRegular = ">";
                            s.token = "mayor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '=':
                            estado = 'A';
                            s.token = "else";
                            s.expresionRegular = "else";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='aqua'>=</font>";
                            s.lexema = "=";
                            s.expresionRegular = "=";
                            s.token = "igual";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '{':
                            estado = 'A';
                            s.token = "else";
                            s.expresionRegular = "else";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>{</font>";
                            s.lexema = "{";
                            s.expresionRegular = "{";
                            s.token = "llave abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '}':
                            estado = 'A';
                            s.token = "else";
                            s.expresionRegular = "else";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>}</font>";
                            s.lexema = "}";
                            s.expresionRegular = "}";
                            s.token = "llave cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '(':
                            estado = 'A';
                            s.token = "else";
                            s.expresionRegular = "else";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>(</font>";
                            s.lexema = "(";
                            s.expresionRegular = "(";
                            s.token = "par. abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ')':
                            estado = 'A';
                            s.token = "else";
                            s.expresionRegular = "else";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>)</font>";
                            s.lexema = ")";
                            s.expresionRegular = ")";
                            s.token = "par. cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ';':
                            estado = 'A';
                            s.token = "else";
                            s.expresionRegular = "else";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='green'>;</font>";
                            s.lexema = ";";
                            s.expresionRegular = ";";
                            s.token = "punto y coma";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ' ':
                            estado = 'A';
                            s.token = "else";
                            s.expresionRegular = "else";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;";
                            break;
                        case '\n':
                            estado = 'A';
                            s.token = "else";
                            s.expresionRegular = "else";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            columna = 0;
                            linea++;
                            codigoHtml += "<br>";
                            break;
                        case '\t':
                            estado = 'A';
                            s.token = "else";
                            s.expresionRegular = "else";
                            codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;&nbsp;&nbsp;";
                            break;
                        default:
                            validarCaracteres = true;
                    }
                    if (validarCaracteres) {
                        if ((letra >= 65 && letra <= 90) || (letra >= 97 && letra <= 122)) {
                            estado = 'E';
                            s.lexema += letra;
                            s.columna = columna;
                            s.linea = linea;
                        } else {
                            if (letra >= 48 && letra <= 57) {
                                estado = 'E';
                                s.lexema += letra;
                                s.columna = columna;
                                s.linea = linea;
                            } else {
                                estado = 'A';
                                s.token = "else";
                                s.expresionRegular = "else";
                                codigoHtml += "<font size='12' color='orange'>" + s.lexema + "</font>";
                                tablaLexico.add(s);
                                s = new Simbolo();
                                String error = "Error lexico linea " + linea + ", columna: " + columna + ", caracter no reconocido: " + letra;
                                errorLexico.add(error);
                            }
                        }

                    }
                    break;
                case 'D':
                    switch (letra) {
                        case 'a':
                            estado = 'K';
                            s.lexema = "va";
                            break;
                        case '_':
                            estado = 'E';
                            s.lexema += "_";
                            break;
                        case '+':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>+</font>";
                            s.lexema = "+";
                            s.expresionRegular = "+";
                            s.token = "mas";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '-':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>-</font>";
                            s.lexema = "-";
                            s.expresionRegular = "-";
                            s.token = "menos";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '*':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>*</font>";
                            s.lexema = "*";
                            s.expresionRegular = "*";
                            s.token = "Por";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '/':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>/</font>";
                            s.lexema = "/";
                            s.expresionRegular = "/";
                            s.token = "Divisor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '<':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&lt;</font>";
                            s.lexema = "<";
                            s.expresionRegular = "<";
                            s.token = "menor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '>':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&gt;</font>";
                            s.lexema = ">";
                            s.expresionRegular = ">";
                            s.token = "mayor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '=':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='aqua'>=</font>";
                            s.lexema = "=";
                            s.expresionRegular = "=";
                            s.token = "igual";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '{':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>{</font>";
                            s.lexema = "{";
                            s.expresionRegular = "{";
                            s.token = "llave abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '}':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>}</font>";
                            s.lexema = "}";
                            s.expresionRegular = "}";
                            s.token = "llave cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '(':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>(</font>";
                            s.lexema = "(";
                            s.expresionRegular = "(";
                            s.token = "par. abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ')':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>)</font>";
                            s.lexema = ")";
                            s.expresionRegular = ")";
                            s.token = "par. cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ';':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='green'>;</font>";
                            s.lexema = ";";
                            s.expresionRegular = ";";
                            s.token = "punto y coma";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ' ':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;";
                            break;
                        case '\n':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            columna = 0;
                            linea++;
                            codigoHtml += "<br>";
                            break;
                        case '\t':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;&nbsp;&nbsp;";
                            break;
                        default:
                            validarCaracteres = true;
                    }
                    if (validarCaracteres) {
                        if ((letra >= 65 && letra <= 90) || (letra >= 97 && letra <= 122)) {
                            estado = 'E';
                            s.lexema += letra;
                            s.columna = columna;
                            s.linea = linea;
                        } else {
                            if (letra >= 48 && letra <= 57) {
                                estado = 'E';
                                s.lexema += letra;
                                s.columna = columna;
                                s.linea = linea;
                            } else {
                                estado = 'A';
                                s.token = "identificador";
                                s.expresionRegular = "L(L|D|_)*";
                                codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                                tablaLexico.add(s);
                                s = new Simbolo();
                                String error = "Error lexico linea " + linea + ", columna: " + columna + ", caracter no reconocido: " + letra;
                                errorLexico.add(error);
                            }
                        }

                    }
                    break;
                case 'K':
                    switch (letra) {
                        case 'r':
                            estado = 'L';
                            s.lexema = "var";
                            break;
                        case '_':
                            estado = 'E';
                            s.lexema += "_";
                            break;
                        case '+':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>+</font>";
                            s.lexema = "+";
                            s.expresionRegular = "+";
                            s.token = "mas";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '-':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>-</font>";
                            s.lexema = "-";
                            s.expresionRegular = "-";
                            s.token = "menos";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '<':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&lt;</font>";
                            s.lexema = "<";
                            s.expresionRegular = "<";
                            s.token = "menor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '>':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&gt;</font>";
                            s.lexema = ">";
                            s.expresionRegular = ">";
                            s.token = "mayor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '=':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='aqua'>=</font>";
                            s.lexema = "=";
                            s.expresionRegular = "=";
                            s.token = "igual";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '{':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>{</font>";
                            s.lexema = "{";
                            s.expresionRegular = "{";
                            s.token = "llave abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '}':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>}</font>";
                            s.lexema = "}";
                            s.expresionRegular = "}";
                            s.token = "llave cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '(':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>(</font>";
                            s.lexema = "(";
                            s.expresionRegular = "(";
                            s.token = "par. abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ')':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>)</font>";
                            s.lexema = ")";
                            s.expresionRegular = ")";
                            s.token = "par. cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ';':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='green'>;</font>";
                            s.lexema = ";";
                            s.expresionRegular = ";";
                            s.token = "punto y coma";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ' ':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;";
                            break;
                        case '\n':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            columna = 0;
                            linea++;
                            codigoHtml += "<br>";
                            break;
                        case '\t':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;&nbsp;&nbsp;";
                            break;
                        default:
                            validarCaracteres = true;
                    }
                    if (validarCaracteres) {
                        if ((letra >= 65 && letra <= 90) || (letra >= 97 && letra <= 122)) {
                            estado = 'E';
                            s.lexema += letra;
                            s.columna = columna;
                            s.linea = linea;
                        } else {
                            if (letra >= 48 && letra <= 57) {
                                estado = 'E';
                                s.lexema += letra;
                                s.columna = columna;
                                s.linea = linea;
                            } else {
                                estado = 'A';
                                s.token = "identificador";
                                s.expresionRegular = "L(L|D|_)*";
                                codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                                tablaLexico.add(s);
                                s = new Simbolo();
                                String error = "Error lexico linea " + linea + ", columna: " + columna + ", caracter no reconocido: " + letra;
                                errorLexico.add(error);
                            }
                        }

                    }
                    break;
                case 'L':
                    switch (letra) {
                        case '_':
                            estado = 'E';
                            s.lexema += "_";
                            break;
                        case '+':
                            estado = 'A';
                            s.token = "var";
                            s.expresionRegular = "var";
                            codigoHtml += "<font size='12' color='red'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>+</font>";
                            s.lexema = "+";
                            s.expresionRegular = "+";
                            s.token = "mas";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '<':
                            estado = 'A';
                            s.token = "var";
                            s.expresionRegular = "var";
                            codigoHtml += "<font size='12' color='red'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&lt;</font>";
                            s.lexema = "<";
                            s.expresionRegular = "<";
                            s.token = "menor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '>':
                            estado = 'A';
                            s.token = "var";
                            s.expresionRegular = "var";
                            codigoHtml += "<font size='12' color='red'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&gt;</font>";
                            s.lexema = ">";
                            s.expresionRegular = ">";
                            s.token = "mayor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '=':
                            estado = 'A';
                            s.token = "var";
                            s.expresionRegular = "var";
                            codigoHtml += "<font size='12' color='red'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='aqua'>=</font>";
                            s.lexema = "=";
                            s.expresionRegular = "=";
                            s.token = "igual";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '{':
                            estado = 'A';
                            s.token = "var";
                            s.expresionRegular = "var";
                            codigoHtml += "<font size='12' color='red'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>{</font>";
                            s.lexema = "{";
                            s.expresionRegular = "{";
                            s.token = "llave abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '}':
                            estado = 'A';
                            s.token = "var";
                            s.expresionRegular = "var";
                            codigoHtml += "<font size='12' color='red'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>}</font>";
                            s.lexema = "}";
                            s.expresionRegular = "}";
                            s.token = "llave cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '(':
                            estado = 'A';
                            s.token = "var";
                            s.expresionRegular = "var";
                            codigoHtml += "<font size='12' color='red'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>(</font>";
                            s.lexema = "(";
                            s.expresionRegular = "(";
                            s.token = "par. abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ')':
                            estado = 'A';
                            s.token = "var";
                            s.expresionRegular = "var";
                            codigoHtml += "<font size='12' color='red'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>)</font>";
                            s.lexema = ")";
                            s.expresionRegular = ")";
                            s.token = "par. cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ';':
                            estado = 'A';
                            s.token = "var";
                            s.expresionRegular = "var";
                            codigoHtml += "<font size='12' color='red'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='green'>;</font>";
                            s.lexema = ";";
                            s.expresionRegular = ";";
                            s.token = "punto y coma";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ' ':
                            estado = 'A';
                            s.token = "var";
                            s.expresionRegular = "var";
                            codigoHtml += "<font size='12' color='red'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;";
                            break;
                        case '\n':
                            estado = 'A';
                            s.token = "var";
                            s.expresionRegular = "var";
                            codigoHtml += "<font size='12' color='red'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            columna = 0;
                            linea++;
                            codigoHtml += "<br>";
                            break;
                        case '\t':
                            estado = 'A';
                            s.token = "var";
                            s.expresionRegular = "var";
                            codigoHtml += "<font size='12' color='red'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;&nbsp;&nbsp;";
                            break;
                        default:
                            validarCaracteres = true;
                    }
                    if (validarCaracteres) {
                        if ((letra >= 65 && letra <= 90) || (letra >= 97 && letra <= 122)) {
                            estado = 'E';
                            s.lexema += letra;
                            s.columna = columna;
                            s.linea = linea;
                        } else {
                            if (letra >= 48 && letra <= 57) {
                                estado = 'E';
                                s.lexema += letra;
                                s.columna = columna;
                                s.linea = linea;
                            } else {
                                estado = 'A';
                                s.token = "var";
                                s.expresionRegular = "var";
                                codigoHtml += "<font size='12' color='red'>" + s.lexema + "</font>";
                                tablaLexico.add(s);
                                s = new Simbolo();
                                String error = "Error lexico linea " + linea + ", columna: " + columna + ", caracter no reconocido: " + letra;
                                errorLexico.add(error);
                            }
                        }

                    }
                    break;
                case 'F':
                    switch (letra) {
                        case '+':
                            estado = 'A';
                            s.token = "numero";
                            s.expresionRegular = "D+";
                            codigoHtml += "<font size='12' color='fuchsia'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>+</font>";
                            s.lexema = "+";
                            s.expresionRegular = "+";
                            s.token = "mas";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '<':
                            estado = 'A';
                            s.token = "numero";
                            s.expresionRegular = "D+";
                            codigoHtml += "<font size='12' color='fuchsia'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&lt;</font>";
                            s.lexema = "<";
                            s.expresionRegular = "<";
                            s.token = "menor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '>':
                            estado = 'A';
                            s.token = "numero";
                            s.expresionRegular = "D+";
                            codigoHtml += "<font size='12' color='fuchsia'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&gt;</font>";
                            s.lexema = ">";
                            s.expresionRegular = ">";
                            s.token = "mayor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '=':
                            estado = 'A';
                            s.token = "numero";
                            s.expresionRegular = "D+";
                            codigoHtml += "<font size='12' color='fuchsia'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='aqua'>=</font>";
                            s.lexema = "=";
                            s.expresionRegular = "=";
                            s.token = "igual";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '{':
                            estado = 'A';
                            s.token = "numero";
                            s.expresionRegular = "D+";
                            codigoHtml += "<font size='12' color='fuchsia'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>{</font>";
                            s.lexema = "{";
                            s.expresionRegular = "{";
                            s.token = "llave abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '}':
                            estado = 'A';
                            s.token = "numero";
                            s.expresionRegular = "D+";
                            codigoHtml += "<font size='12' color='fuchsia'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>}</font>";
                            s.lexema = "}";
                            s.expresionRegular = "}";
                            s.token = "llave cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '(':
                            estado = 'A';
                            s.token = "numero";
                            s.expresionRegular = "D+";
                            codigoHtml += "<font size='12' color='fuchsia'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>(</font>";
                            s.lexema = "(";
                            s.expresionRegular = "(";
                            s.token = "par. abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '*':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>*</font>";
                            s.lexema = "*";
                            s.expresionRegular = "*";
                            s.token = "Por";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ')':
                            estado = 'A';
                            s.token = "numero";
                            s.expresionRegular = "D+";
                            codigoHtml += "<font size='12' color='fuchsia'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>)</font>";
                            s.lexema = ")";
                            s.expresionRegular = ")";
                            s.token = "par. cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ';':
                            estado = 'A';
                            s.token = "numero";
                            s.expresionRegular = "D+";
                            codigoHtml += "<font size='12' color='fuchsia'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='green'>;</font>";
                            s.lexema = ";";
                            s.expresionRegular = ";";
                            s.token = "punto y coma";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ' ':
                            estado = 'A';
                            s.token = "numero";
                            s.expresionRegular = "D+";
                            codigoHtml += "<font size='12' color='fuchsia'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;";
                            break;
                        case '\n':
                            estado = 'A';
                            s.token = "numero";
                            s.expresionRegular = "D+";
                            codigoHtml += "<font size='12' color='fuchsia'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            columna = 0;
                            linea++;
                            codigoHtml += "<br>";
                            break;
                        case '\t':
                            estado = 'A';
                            s.token = "numero";
                            s.expresionRegular = "D+";
                            codigoHtml += "<font size='12' color='fuchsia'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;&nbsp;&nbsp;";
                            break;
                        default:
                            validarCaracteres = true;
                    }
                    if (validarCaracteres) {
                        if (letra >= 48 && letra <= 57) {
                            estado = 'E';
                            s.lexema += letra;
                            s.columna = columna;
                            s.linea = linea;
                        } else {
                            estado = 'A';
                            s.token = "numero";
                            s.expresionRegular = "D+";
                            codigoHtml += "<font size='12' color='fuchsia'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            String error = "Error lexico linea " + linea + ", columna: " + columna + ", caracter no reconocido: " + letra;
                            errorLexico.add(error);
                        }

                    }
                    break;
                case 'E':
                    switch (letra) {
                        case '_':
                            estado = 'E';
                            s.lexema += "_";
                            break;
                        case '+':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>+</font>";
                            s.lexema = "+";
                            s.expresionRegular = "+";
                            s.token = "mas";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '<':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&lt;</font>";
                            s.lexema = "<";
                            s.expresionRegular = "<";
                            s.token = "menor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '>':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='yellow'>&gt;</font>";
                            s.lexema = ">";
                            s.expresionRegular = ">";
                            s.token = "mayor";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '=':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='aqua'>=</font>";
                            s.lexema = "=";
                            s.expresionRegular = "=";
                            s.token = "igual";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '{':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>{</font>";
                            s.lexema = "{";
                            s.expresionRegular = "{";
                            s.token = "llave abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '}':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>}</font>";
                            s.lexema = "}";
                            s.expresionRegular = "}";
                            s.token = "llave cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case '(':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>(</font>";
                            s.lexema = "(";
                            s.expresionRegular = "(";
                            s.token = "par. abre";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ')':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='purple'>)</font>";
                            s.lexema = ")";
                            s.expresionRegular = ")";
                            s.token = "par. cierra";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ';':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "<font size='12' color='green'>;</font>";
                            s.lexema = ";";
                            s.expresionRegular = ";";
                            s.token = "punto y coma";
                            s.columna = columna;
                            s.linea = linea;
                            tablaLexico.add(s);
                            s = new Simbolo();
                            break;
                        case ' ':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;";
                            break;
                        case '\n':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            columna = 0;
                            linea++;
                            codigoHtml += "<br>";
                            break;
                        case '\t':
                            estado = 'A';
                            s.token = "identificador";
                            s.expresionRegular = "L(L|D|_)*";
                            codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                            tablaLexico.add(s);
                            s = new Simbolo();
                            codigoHtml += "&nbsp;&nbsp;&nbsp;";
                            break;
                        default:
                            validarCaracteres = true;
                    }
                    if (validarCaracteres) {
                        if ((letra >= 65 && letra <= 90) || (letra >= 97 && letra <= 122)) {
                            estado = 'E';
                            s.lexema += letra;
                            s.columna = columna;
                            s.linea = linea;
                        } else {
                            if (letra >= 48 && letra <= 57) {
                                estado = 'E';
                                s.lexema += letra;
                                s.columna = columna;
                                s.linea = linea;
                            } else {
                                estado = 'A';
                                s.token = "identificador";
                                s.expresionRegular = "L(L|D|_)*";
                                codigoHtml += "<font size='12' color='blue'>" + s.lexema + "</font>";
                                tablaLexico.add(s);
                                s = new Simbolo();
                                String error = "Error lexico linea " + linea + ", columna: " + columna + ", caracter no reconocido: " + letra;
                                errorLexico.add(error);
                            }
                        }

                    }
                    break;
            }
        }

        return codigoHtml;

    }

}
