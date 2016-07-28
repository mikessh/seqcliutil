package com.antigenomics.seqcliutil;

import com.milaboratory.core.io.sequence.SingleRead;
import com.milaboratory.core.io.sequence.SingleReader;
import com.milaboratory.core.sequence.NucleotideSequence;

import java.util.ArrayList;
import java.util.List;

public abstract class IterableNucleotideDb implements ReadDb {
    protected final List<NucleotideSequence> sequences = new ArrayList<>();
    protected final boolean fullLengthMatching;

    public IterableNucleotideDb(boolean fullLengthMatching) {
        this.fullLengthMatching = fullLengthMatching;
    }

    @Override
    public void addReads(SingleReader reader) {
        SingleRead read;

        while ((read = reader.take()) != null)
            sequences.add(read.getData().getSequence());
    }
}
