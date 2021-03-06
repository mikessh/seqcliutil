package com.antigenomics.seqcliutil;

import com.milaboratory.core.io.sequence.SingleRead;
import com.milaboratory.core.motif.BitapPattern;

public class BitapReadDb extends IterableNucleotideDb {
    private final float mismatchFraction;

    public BitapReadDb(boolean fullLengthMatching, float mismatchFraction) {
        super(fullLengthMatching);
        this.mismatchFraction = mismatchFraction;

        if (mismatchFraction < 0 || mismatchFraction > 1) {
            throw new IllegalArgumentException("Parameter 'mismatchFraction' should be in [0, 1].");
        }
    }

    @Override
    public boolean contains(SingleRead record) {
        final BitapPattern pattern = record.getData().getSequence().toMotif().getBitapPattern();
        final int len = record.getData().size();
        final int mismatches = (int) (len * mismatchFraction);

        return fullLengthMatching ?
                sequences.parallelStream().anyMatch(dbSeq -> {
                    int lenDiff = len - dbSeq.size();
                    int mms = mismatches - (lenDiff < 0 ? -lenDiff : lenDiff);

                    return mms >= 0 && pattern.substitutionAndIndelMatcherLast(mms, dbSeq).findNext() >= 0;
                }) :
                sequences.parallelStream().anyMatch(dbSeq ->
                        pattern.substitutionAndIndelMatcherLast(mismatches, dbSeq).findNext() >= 0);
    }
}