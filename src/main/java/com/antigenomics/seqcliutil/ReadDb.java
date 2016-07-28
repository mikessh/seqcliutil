package com.antigenomics.seqcliutil;

import com.milaboratory.core.io.sequence.SingleRead;
import com.milaboratory.core.io.sequence.SingleReader;

public interface ReadDb {
    void addReads(SingleReader reader);

    boolean contains(SingleRead record);
}
