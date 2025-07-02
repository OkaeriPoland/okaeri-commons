/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017. Diorite (by Bartłomiej Mazur (aka GotoFinal))
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
package eu.okaeri.commons.range;

import eu.okaeri.commons.Numbers;
import eu.okaeri.commons.RandomNumbers;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * Class defining range in floats, may be used to validate numbers.
 */
@Data(staticConstructor = "of")
@SuppressWarnings({"FieldNamingConvention", "QuestionableName", "StandardVariableNames", "BooleanMethodIsAlwaysInverted"})
public class FloatRange {

    /**
     * Range from 0 to 0.
     */
    public static final FloatRange EMPTY = new FloatRange(0, 0);

    private static final float EPSILON = 1e-6f;

    private final float min;
    private final float max;

    /**
     * Construct new range.
     *
     * @param min min value of range.
     * @param max max value of range.
     */
    public FloatRange(float min, float max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Construct new range.
     *
     * @param min min value of range.
     * @param max max value of range.
     */
    public FloatRange(double min, double max) {
        this((float) min, (float) max);
    }

    /**
     * @return random value in range.
     */
    public float getRandom() {
        if ((this.min == this.max) || (Math.abs(this.min - this.max) < EPSILON)) return this.min;
        return RandomNumbers.nextFloat(this.min, this.max);
    }

    /**
     * Returns random value in range.
     *
     * @param random random instance to use.
     * @return random value in range.
     */
    public float getRandom(Random random) {
        if ((this.min == this.max) || (Math.abs(this.min - this.max) < EPSILON)) return this.min;
        return RandomNumbers.nextFloat(random, this.min, this.max);
    }

    /**
     * @return size of range. (max - min)
     */
    public double size() {
        return ((double) this.max - (double) this.min);
    }

    /**
     * Check if given number is in range.
     *
     * @param i number to check.
     * @return true if it is in range
     */
    public boolean isIn(float i) {
        return (i >= this.min) && (i <= this.max);
    }

    /**
     * Check if given number is in range.
     *
     * @param i number to check.
     * @return true if it is in range
     */
    public boolean isIn(double i) {
        return (i >= this.min) && (i <= this.max);
    }

    /**
     * Return given number if it is in range, or closest value in range.
     * {@code i > max -> max}
     * {@code i < min -> min}
     * {@code else -> i}
     *
     * @param i number to validate.
     * @return closest number in range.
     */
    public double getIn(float i) {
        if (i > this.max) {
            return this.max;
        }
        if (i < this.min) {
            return this.min;
        }
        return i;
    }

    /**
     * Return given number if it is in range, or default value.
     * {@code i > max -> def}
     * {@code i < min -> def}
     * {@code else -> i}
     *
     * @param i   number to validate.
     * @param def default value.
     * @return given number or default value.
     */
    public double getIn(float i, float def) {
        if (!this.isIn(i)) {
            return def;
        }
        return i;
    }

    /**
     * Return given number if it is in range, or default value.
     * {@code i > max -> def}
     * {@code i < min -> def}
     * {@code else -> i}
     *
     * @param i   number to validate.
     * @param def default value.
     * @return given number or default value.
     */
    public double getIn(float i, double def) {
        if (!this.isIn(i)) {
            return def;
        }
        return i;
    }

    /**
     * Return given number if it is in range, or closest value in range.
     * {@code i > max -> max}
     * {@code i < min -> min}
     * {@code else -> i}
     *
     * @param i number to validate.
     * @return closest number in range.
     */
    public float getIn(double i) {
        if (i > this.max) {
            return this.max;
        }
        if (i < this.min) {
            return this.min;
        }
        return (float) i;
    }

    /**
     * Return given number if it is in range, or closest value in range.
     * {@code i > max -> def}
     * {@code i < min -> def}
     * {@code else -> i}
     *
     * @param i   number to validate.
     * @param def default value.
     * @return closest number in range.
     */
    public float getIn(double i, double def) {
        if (!this.isIn(i)) {
            return (float) def;
        }
        return (float) i;
    }

    /**
     * Create range with only gived value in range.
     *
     * @param num min and max of range.
     * @return range with only one value in range.
     */
    public static FloatRange fixed(float num) {
        return new FloatRange(num, num);
    }

    /**
     * Create range with only gived value in range.
     *
     * @param num min and max of range.
     * @return range with only one value in range.
     */
    public static FloatRange fixed(double num) {
        return new FloatRange(num, num);
    }

    /**
     * Parses given string to range, string is valid range when contains 2 numbers (second greater than first) and splt char: <br>
     * " - ", " : ", " ; ", ", ", " ", ",", ";", ":", "-"
     *
     * @param string string to parse.
     * @return parsed range or null.
     */
    @Nullable
    public static FloatRange valueOf(@Nullable String string) {
        if ((string == null) || string.isEmpty()) {
            return null;
        }
        String[] nums = null;
        int i = 0;
        boolean firstMinus = string.charAt(0) == '-';
        if (firstMinus) {
            string = string.substring(1);
        }
        while ((i < ByteRange.SPLITS.size()) && ((nums == null) || (nums.length != 2))) {
            nums = string.split(ByteRange.SPLITS.get(i++), 2);
        }
        if ((nums == null) || (nums.length != 2)) {
            return null;
        }
        Float min = Numbers.asFloat(firstMinus ? ("-" + nums[0]) : nums[0]);
        if (min == null) {
            return null;
        }
        Float max = Numbers.asFloat(nums[1]);
        if ((max == null) || (min > max)) {
            return null;
        }
        return new FloatRange(min, max);
    }
}
