package gymtech.util;

import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Classe utilitária para formatação e limpeza de campos como CPF, Telefone, Data e CEP.
 */
public class Formatacao {

    // --- FORMATAÇÃO DE STRING ---

    /**
     * Formata uma string de dígitos como número decimal PT-BR (ex: "1.234,56").
     */
    public static String formatarMoeda(String valorLimpo) {
        if (valorLimpo == null || valorLimpo.isEmpty()) {
            return "0,00";
        }
        long centavos;
        try {
            centavos = Long.parseLong(valorLimpo);
        } catch (NumberFormatException e) {
            centavos = 0;
        }
        double valorReal = centavos / 100.0;
        java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance(new java.util.Locale("pt", "BR"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(valorReal);
    }

    /**
     * Formata um BigDecimal para string de moeda PT-BR (ex: 1234.56 -> "1.234,56").
     */
    public static String formatarMoeda(java.math.BigDecimal valor) {
        if (valor == null) {
            valor = java.math.BigDecimal.ZERO;
        }
        java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance(new java.util.Locale("pt", "BR"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(valor);
    }

    /**
     * Formata um double para string de moeda PT-BR (ex: 1234.56 -> "1.234,56").
     */
    public static String formatarMoeda(double valor) {
        java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance(new java.util.Locale("pt", "BR"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(valor);
    }

    /**
     * Formata uma string de CPF (adiciona pontos e traço).
     * Exemplo: "12345678901" -> "123.456.789-01"
     */
    public static String formatarCpf(String cpf) {
        if (cpf == null) return "";
        String apenasNumeros = limpar(cpf);
        if (apenasNumeros.length() > 11) {
            apenasNumeros = apenasNumeros.substring(0, 11);
        }
        int len = apenasNumeros.length();
        if (len <= 3) {
            return apenasNumeros;
        } else if (len <= 6) {
            return apenasNumeros.substring(0, 3) + "." + apenasNumeros.substring(3);
        } else if (len <= 9) {
            return apenasNumeros.substring(0, 3) + "." + apenasNumeros.substring(3, 6) + "." + apenasNumeros.substring(6);
        } else {
            return apenasNumeros.substring(0, 3) + "." + apenasNumeros.substring(3, 6) + "." + apenasNumeros.substring(6, 9) + "-" + apenasNumeros.substring(9);
        }
    }

    /**
     * Formata uma string de Telefone (adiciona parênteses, espaço e traço).
     * Suporta telefones fixos (8 dígitos) e celulares (9 dígitos).
     * Exemplos: "11987654321" -> "(11) 98765-4321", "1187654321" -> "(11) 8765-4321"
     */
    public static String formatarTelefone(String telefone) {
        if (telefone == null) return "";
        String apenasNumeros = limpar(telefone);
        if (apenasNumeros.length() > 11) {
            apenasNumeros = apenasNumeros.substring(0, 11);
        }
        int len = apenasNumeros.length();
        if (len <= 2) {
            return apenasNumeros;
        } else if (len <= 6) {
            return "(" + apenasNumeros.substring(0, 2) + ") " + apenasNumeros.substring(2);
        } else if (len <= 10) {
            return "(" + apenasNumeros.substring(0, 2) + ") " + apenasNumeros.substring(2, 6) + "-" + apenasNumeros.substring(6);
        } else {
            return "(" + apenasNumeros.substring(0, 2) + ") " + apenasNumeros.substring(2, 7) + "-" + apenasNumeros.substring(7);
        }
    }

    /**
     * Formata uma string de Data (adiciona barras).
     * Exemplo: "15052000" -> "15/05/2000"
     */
    public static String formatarData(String data) {
        if (data == null) return "";
        String apenasNumeros = limpar(data);
        if (apenasNumeros.length() > 8) {
            apenasNumeros = apenasNumeros.substring(0, 8);
        }
        int len = apenasNumeros.length();
        if (len <= 2) {
            return apenasNumeros;
        } else if (len <= 4) {
            return apenasNumeros.substring(0, 2) + "/" + apenasNumeros.substring(2);
        } else {
            return apenasNumeros.substring(0, 2) + "/" + apenasNumeros.substring(2, 4) + "/" + apenasNumeros.substring(4);
        }
    }

    /**
     * Formata uma string de CEP (adiciona traço).
     * Exemplo: "01001000" -> "01001-000"
     */
    public static String formatarCep(String cep) {
        if (cep == null) return "";
        String apenasNumeros = limpar(cep);
        if (apenasNumeros.length() > 8) {
            apenasNumeros = apenasNumeros.substring(0, 8);
        }
        int len = apenasNumeros.length();
        if (len <= 5) {
            return apenasNumeros;
        } else {
            return apenasNumeros.substring(0, 5) + "-" + apenasNumeros.substring(5);
        }
    }

    /**
     * Formata uma string de CNPJ (adiciona pontos, barra e traço).
     * Exemplo: "12345678000199" -> "12.345.678/0001-99"
     */
    public static String formatarCnpj(String cnpj) {
        if (cnpj == null) return "";
        String apenasNumeros = limpar(cnpj);
        if (apenasNumeros.length() > 14) {
            apenasNumeros = apenasNumeros.substring(0, 14);
        }
        int len = apenasNumeros.length();
        if (len <= 2) {
            return apenasNumeros;
        } else if (len <= 5) {
            return apenasNumeros.substring(0, 2) + "." + apenasNumeros.substring(2);
        } else if (len <= 8) {
            return apenasNumeros.substring(0, 2) + "." + apenasNumeros.substring(2, 5) + "." + apenasNumeros.substring(5);
        } else if (len <= 12) {
            return apenasNumeros.substring(0, 2) + "." + apenasNumeros.substring(2, 5) + "." + apenasNumeros.substring(5, 8) + "/" + apenasNumeros.substring(8);
        } else {
            return apenasNumeros.substring(0, 2) + "." + apenasNumeros.substring(2, 5) + "." + apenasNumeros.substring(5, 8) + "/" + apenasNumeros.substring(8, 12) + "-" + apenasNumeros.substring(12);
        }
    }

    // --- REMOVER FORMATAÇÃO (LIMPEZA) ---

    /**
     * Converte um valor formatado (ex: "1.234,56") de volta para BigDecimal (ex: 1234.56).
     */
    public static java.math.BigDecimal limparMoeda(String valorFormatado) {
        if (valorFormatado == null || valorFormatado.trim().isEmpty()) {
            return java.math.BigDecimal.ZERO;
        }
        String limpo = valorFormatado
                .replaceAll("\\s+", "")
                .replace(".", "")
                .replace(",", ".");
        try {
            return new java.math.BigDecimal(limpo);
        } catch (NumberFormatException e) {
            return java.math.BigDecimal.ZERO;
        }
    }

    /**
     * Limpa a string, removendo qualquer caractere que não seja número.
     * Exemplo: "(11) 98765-4321" -> "11987654321"
     */
    public static String limpar(String texto) {
        if (texto == null) return "";
        return texto.replaceAll("\\D", "");
    }

    // --- ADICIONADORES DE MÁSCARA DINÂMICA EM TEXTFIELDS ---

    /**
     * Adiciona formatação de CPF em tempo real a um JTextField.
     */
    public static void adicionarMascaraCpf(final JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                formatarCampoDinamico(campo, TipoMascara.CPF);
            }
        });
    }

    /**
     * Adiciona formatação de Telefone em tempo real a um JTextField.
     */
    public static void adicionarMascaraTelefone(final JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                formatarCampoDinamico(campo, TipoMascara.TELEFONE);
            }
        });
    }

    /**
     * Adiciona formatação de Data em tempo real a um JTextField.
     */
    public static void adicionarMascaraData(final JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                formatarCampoDinamico(campo, TipoMascara.DATA);
            }
        });
    }

    /**
     * Adiciona formatação de CEP em tempo real a um JTextField.
     */
    public static void adicionarMascaraCep(final JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                formatarCampoDinamico(campo, TipoMascara.CEP);
            }
        });
    }

    /**
     * Adiciona formatação de CNPJ em tempo real a um JTextField.
     */
    public static void adicionarMascaraCnpj(final JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                formatarCampoDinamico(campo, TipoMascara.CNPJ);
            }
        });
    }

    /**
     * Adiciona formatação de moeda (PT-BR decimal) em tempo real a um JTextField.
     */
    public static void adicionarMascaraMoeda(final JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                formatarCampoDinamico(campo, TipoMascara.MOEDA);
            }
        });
    }

    private enum TipoMascara {
        CPF, TELEFONE, DATA, CEP, CNPJ, MOEDA
    }

    private static void formatarCampoDinamico(JTextField campo, TipoMascara tipo) {
        String textoOriginal = campo.getText();
        String apenasNumeros = limpar(textoOriginal);
        String formatado;

        switch (tipo) {
            case CPF:
                formatado = formatarCpf(apenasNumeros);
                break;
            case TELEFONE:
                formatado = formatarTelefone(apenasNumeros);
                break;
            case DATA:
                formatado = formatarData(apenasNumeros);
                break;
            case CEP:
                formatado = formatarCep(apenasNumeros);
                break;
            case CNPJ:
                formatado = formatarCnpj(apenasNumeros);
                break;
            case MOEDA:
                formatado = formatarMoeda(apenasNumeros);
                break;
            default:
                formatado = textoOriginal;
        }

        if (!textoOriginal.equals(formatado)) {
            int cursor = campo.getCaretPosition();
            int oldLen = textoOriginal.length();
            campo.setText(formatado);
            int newLen = formatado.length();
            int newCursor = cursor + (newLen - oldLen);
            if (newCursor >= 0 && newCursor <= newLen) {
                campo.setCaretPosition(newCursor);
            } else {
                campo.setCaretPosition(newLen);
            }
        }
    }
}
