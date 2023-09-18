package com.runwithme.runwithme.domain.record.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChallengeTotalRecord is a Querydsl query type for ChallengeTotalRecord
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChallengeTotalRecord extends EntityPathBase<ChallengeTotalRecord> {

    private static final long serialVersionUID = 701443187L;

    public static final QChallengeTotalRecord challengeTotalRecord = new QChallengeTotalRecord("challengeTotalRecord");

    public final NumberPath<Long> challengeSeq = createNumber("challengeSeq", Long.class);

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final NumberPath<Long> totalAvgSpeed = createNumber("totalAvgSpeed", Long.class);

    public final NumberPath<Long> totalCalorie = createNumber("totalCalorie", Long.class);

    public final NumberPath<Long> totalDistance = createNumber("totalDistance", Long.class);

    public final NumberPath<Long> totalLongestDistance = createNumber("totalLongestDistance", Long.class);

    public final NumberPath<Long> totalLongestTime = createNumber("totalLongestTime", Long.class);

    public final NumberPath<Long> totalTime = createNumber("totalTime", Long.class);

    public final NumberPath<Long> userSeq = createNumber("userSeq", Long.class);

    public QChallengeTotalRecord(String variable) {
        super(ChallengeTotalRecord.class, forVariable(variable));
    }

    public QChallengeTotalRecord(Path<? extends ChallengeTotalRecord> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChallengeTotalRecord(PathMetadata metadata) {
        super(ChallengeTotalRecord.class, metadata);
    }

}

