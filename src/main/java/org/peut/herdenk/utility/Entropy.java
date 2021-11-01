package org.peut.herdenk.utility;

import java.util.HashMap;
import java.util.Map;

// used to check strength of password. theoretically 0-8,
// practically max ~4, as text is used. >3 gives ok-ish passwords
// docs at and code from https://rosettacode.org/wiki/Entropy#Java

public class Entropy {

        public static double getEntropy(String s) {
            int n = 0;
            java.util.Map<Character, Integer> occ = new HashMap<>();

            for (int c_ = 0; c_ < s.length(); ++c_) {
                char cx = s.charAt(c_);
                if (occ.containsKey(cx)) {
                    occ.put(cx, occ.get(cx) + 1);
                } else {
                    occ.put(cx, 1);
                }
                ++n;
            }

            double e = 0.0;
            for (Map.Entry<Character, Integer> entry : occ.entrySet()) {
                char cx = entry.getKey();
                double p = (double) entry.getValue() / n;
                e += p * log2(p);
            }
            return -e;
        }

        private static double log2(double a) {
            return Math.log(a) / Math.log(2);
        }

}
