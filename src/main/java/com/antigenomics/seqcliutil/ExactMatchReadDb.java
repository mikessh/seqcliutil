package com.antigenomics.seqcliutil;

import com.milaboratory.core.io.sequence.SingleRead;
import com.milaboratory.core.sequence.NucleotideSequence;

public class ExactMatchReadDb extends IterableNucleotideDb {
    public ExactMatchReadDb(boolean fullLengthMatching) {
        super(fullLengthMatching);
    }

    @Override
    public boolean contains(SingleRead record) {
        final NucleotideSequence query = record.getData().getSequence();

        return fullLengthMatching ?
                sequences.parallelStream().anyMatch(query::equals) :
                sequences.parallelStream().anyMatch(dbSeq -> Util.contains(dbSeq, query));
    }
}