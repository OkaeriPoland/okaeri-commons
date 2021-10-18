/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016. Diorite (by Bartłomiej Mazur (aka GotoFinal))
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package eu.okaeri.commons;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"SameParameterValue"})
public class RomanNumeral {

    private static final Map<Integer, String> BASE_VALUES = new HashMap<>(32);
    private final int num;
    private transient String romanStr;

    static {
        BASE_VALUES.put(0, "");
        BASE_VALUES.put(1, "I");
        BASE_VALUES.put(2, "II");
        BASE_VALUES.put(3, "III");
        BASE_VALUES.put(4, "IV");
        BASE_VALUES.put(5, "V");
        BASE_VALUES.put(6, "VI");
        BASE_VALUES.put(7, "VII");
        BASE_VALUES.put(8, "VIII");
        BASE_VALUES.put(9, "IX");
        BASE_VALUES.put(10, "X");
        BASE_VALUES.put(20, "XX");
        BASE_VALUES.put(30, "XXX");
        BASE_VALUES.put(40, "XL");
        BASE_VALUES.put(50, "L");
        BASE_VALUES.put(60, "LX");
        BASE_VALUES.put(70, "LXX");
        BASE_VALUES.put(80, "LXXX");
        BASE_VALUES.put(90, "XC");
        BASE_VALUES.put(100, "C");
        BASE_VALUES.put(200, "CC");
        BASE_VALUES.put(300, "CCC");
        BASE_VALUES.put(400, "CD");
        BASE_VALUES.put(500, "D");
        BASE_VALUES.put(600, "DC");
        BASE_VALUES.put(700, "DCC");
        BASE_VALUES.put(800, "DCCC");
        BASE_VALUES.put(900, "CM");
        BASE_VALUES.put(1000, "M");
    }

    public RomanNumeral(int arabic, boolean valid) {
        if (valid && (arabic < 0)) {
            throw new NumberFormatException("Value of RomanNumeral must be positive.");
        }
        if (valid && (arabic > 3999)) {
            throw new NumberFormatException("Value of RomanNumeral must be 3999 or less.");
        }
        this.num = arabic;
    }

    public RomanNumeral(int arabic) {
        this(arabic, true);
    }

    public RomanNumeral(@NonNull String roman, boolean safeForm) {
        if (safeForm) {
            this.romanStr = roman;
        }
        this.num = toInt(roman);
    }

    public RomanNumeral(@NonNull String roman) {
        this(roman, false);
    }

    public RomanNumeral add(@NonNull RomanNumeral other) {
        return new RomanNumeral(this.num + other.num, false);
    }

    public RomanNumeral subtract(@NonNull RomanNumeral other) {
        return new RomanNumeral(this.num - other.num, false);
    }

    public RomanNumeral multiple(@NonNull RomanNumeral other) {
        return new RomanNumeral(this.num * other.num, false);
    }

    public RomanNumeral divide(@NonNull RomanNumeral other) {
        return new RomanNumeral(this.num / other.num, false);
    }

    @Override
    public String toString() {
        if (this.romanStr == null) {
            this.romanStr = toString(this.num);
        }
        return this.romanStr;
    }

    public int toInt() {
        return this.num;
    }

    private static int letterToNumber(char letter) {
        switch (letter) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                throw new NumberFormatException("Illegal character \"" + letter + "\" in Roman numeral");
        }
    }

    private static int pow(int i, int pow) {
        if (pow == 0) {
            return 1;
        }
        if (pow == 1) {
            return i;
        }
        for (int k = 0; k < (pow - 1); k++) {
            i *= i;
        }
        return i;
    }

    public static int toInt(@NonNull String roman) throws NumberFormatException {
        boolean negative = roman.startsWith("-");
        if (negative) {
            roman = roman.substring(1);
        }
        if (roman.isEmpty()) {
            return 0;
        }
        char[] charArray = roman.toUpperCase().toCharArray();
        int lastValue = 0;
        int lastMulti = 1;
        int result = 0;
        for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; i++) {
            char c = charArray[i];
            int value = letterToNumber(c);
            if (i == 0) {
                if ((i + 1) >= charArrayLength) {
                    result += value;
                    break;
                }
                lastValue = value;
                continue;
            }
            if (value == lastValue) {
                lastMulti++;
                continue;
            }
            if (lastValue < value) {
                if (lastMulti != 0) {
                    result += (value - (lastValue * lastMulti));
                } else {
                    result += (value - (lastValue << 1));
                }
            } else {
                result += lastValue * lastMulti;
                result += value;
            }
            lastMulti = 0;
            lastValue = value;

        }
        if (lastMulti != 0) {
            result += (lastValue * lastMulti);
        }
        return negative ? -result : result;
    }

    public static String toString(int num) {
        boolean negative = false;
        if (num < 0) {
            num *= -1;
            negative = true;
        }
        String romanStr = BASE_VALUES.get(num);
        StringBuilder roman;
        if (romanStr == null) {
            roman = new StringBuilder(negative ? "-" : "");
        } else {
            if (negative) {
                return "-" + romanStr;
            }
            return romanStr;
        }
        while (num >= 1000) {
            roman.append("M");
            num -= 1000;
        }
        if (num == 0) {
            return roman.toString();
        }
        String str = Integer.toString(num);
        char[] charArray = str.toCharArray();
        String[] romanParts = new String[charArray.length];
        int k = 0;
        for (int i = charArray.length - 1; i >= 0; i--) {
            char c = charArray[i];
            switch (c) {
                case '1':
                    romanParts[i] = BASE_VALUES.get(pow(10, k));
                    break;
                case '2':
                    romanParts[i] = BASE_VALUES.get(2 * pow(10, k));
                    break;
                case '3':
                    romanParts[i] = BASE_VALUES.get(3 * pow(10, k));
                    break;
                case '4':
                    romanParts[i] = BASE_VALUES.get(4 * pow(10, k));
                    break;
                case '5':
                    romanParts[i] = BASE_VALUES.get(5 * pow(10, k));
                    break;
                case '6':
                    romanParts[i] = BASE_VALUES.get(6 * pow(10, k));
                    break;
                case '7':
                    romanParts[i] = BASE_VALUES.get(7 * pow(10, k));
                    break;
                case '8':
                    romanParts[i] = BASE_VALUES.get(8 * pow(10, k));
                    break;
                case '9':
                    romanParts[i] = BASE_VALUES.get(9 * pow(10, k));
                    break;
                default:
                    break;
            }
            k++;
        }
        return roman.append(String.join("", romanParts)).toString();
    }
}
