package eu.okaeri.commons;

public final class Numbers {

    public static boolean isInteger(String text) {

        if (text.isEmpty()) {
            return false;
        }

        for (int i = 0; i < text.length(); i++) {

            if ((i == 0) && (text.charAt(i) == '-')) {
                if (text.length() == 1) {
                    return false;
                } else {
                    continue;
                }
            }

            if (Character.digit(text.charAt(i), 10) < 0) {
                return false;
            }
        }

        return true;
    }
}
