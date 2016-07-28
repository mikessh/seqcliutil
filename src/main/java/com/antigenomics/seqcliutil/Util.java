package com.antigenomics.seqcliutil;

import com.milaboratory.core.sequence.NucleotideSequence;

public class Util {
    static boolean contains(NucleotideSequence source, NucleotideSequence target) {
        if (target.size() == 0 || source.size() == 0) {
            return false;
        }

        byte first = target.codeAt(0);
        int max = source.size() - target.size();

        for (int i = 0; i <= max; i++) {
            /* Look for first character. */
            if (source.codeAt(i) != first) {
                while (++i <= max && source.codeAt(i) != first) ;
            }

            /* Found first character, now look at the rest of v2 */
            if (i <= max) {
                int j = i + 1;
                int end = j + target.size() - 1;
                for (int k = 1; j < end && source.codeAt(j) == target.codeAt(k); j++, k++) ;

                if (j == end) {
                    /* Found whole string. */
                    return true;
                }
            }
        }

        return false;
    }
}
