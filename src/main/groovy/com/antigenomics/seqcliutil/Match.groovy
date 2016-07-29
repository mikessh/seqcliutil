package com.antigenomics.seqcliutil

import com.milaboratory.core.io.sequence.fasta.FastaReader
import com.milaboratory.core.io.sequence.fasta.FastaRecord
import com.milaboratory.core.io.sequence.fasta.FastaSequenceReaderWrapper
import com.milaboratory.core.io.sequence.fasta.FastaWriter
import com.milaboratory.core.sequence.NucleotideSequence

def cli = new CliBuilder(usage: "Match [options] database.fasta[.gz] query.fasta[.gz] output.fasta[.gz]")
cli.h("display help message")
cli.i(longOpt: "identity", args: 1, argName: "[0,1]",
        "Require a match with a certain percent of identity. By default will require exact matching.")
cli.f(longOpt: "full-length", "Perform match against the whole database sequence. " +
        "Will require exact match in case of --exact, " +
        "will penalize for bases that are not covered by query in case of --fuzzy.")
cli.n(longOpt: "negative", "Will report query sequences that do not match any of database sequences " +
        "under the specified matching rule.")

def opt = cli.parse(args)

if (opt == null) {
    System.exit(1)
}

if (opt.h || opt.arguments().size() != 3) {
    cli.usage()
    System.exit(1)
}

def negative = (boolean) opt.'n', fullLength = (boolean) opt.'f'

def db = opt.'i' ? new BitapReadDb(fullLength, (opt.'i').toFloat()) :
        new ExactMatchReadDb(fullLength)

def dbFileName = opt.arguments()[0],
    queryFileName = opt.arguments()[0],
    outputFileName = opt.arguments()[0]

db.addReads(new FastaSequenceReaderWrapper(new FastaReader(dbFileName, NucleotideSequence.ALPHABET), true))

def queryReader = new FastaSequenceReaderWrapper(new FastaReader(queryFileName, NucleotideSequence.ALPHABET), true)
def read

def fastaWriter = new FastaWriter<NucleotideSequence>(outputFileName)

while ((read = queryReader.take()) != null) {
    if (negative ? !db.contains(read) : db.contains(read)) {
        fastaWriter.write(new FastaRecord<NucleotideSequence>(read.id, read.description, read.data.sequence))
    }
}

fastaWriter.close()
