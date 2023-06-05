package com.runwithme.runwithme.domain.record.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRunRecord is a Querydsl query type for RunRecord
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRunRecord extends EntityPathBase<RunRecord> {

    private static final long serialVersionUID = 468921659L;

    public static final QRunRecord runRecord = new QRunRecord("runRecord");

    public final NumberPath<Long> avgSpeed = createNumber("avgSpeed", Long.class);

    public final NumberPath<Long> calorie = createNumber("calorie", Long.class);

    public final NumberPath<Long> challengeSeq = createNumber("challengeSeq", Long.class);

    public final ListPath<RecordCoordinate, QRecordCoordinate> coordinates = this.<RecordCoordinate, QRecordCoordinate>createList("coordinates", RecordCoordinate.class, QRecordCoordinate.class, PathInits.DIRECT2);

    public final StringPath endTime = createString("endTime");

    public final NumberPath<Long> imgSeq = createNumber("imgSeq", Long.class);

    public final DatePath<java.time.LocalDate> regTime = createDate("regTime", java.time.LocalDate.class);

    public final NumberPath<Long> runningDistance = createNumber("runningDistance", Long.class);

    public final NumberPath<Long> runningTime = createNumber("runningTime", Long.class);

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final StringPath startTime = createString("startTime");

    public final NumberPath<Long> userSeq = createNumber("userSeq", Long.class);

    public QRunRecord(String variable) {
        super(RunRecord.class, forVariable(variable));
    }

    public QRunRecord(Path<? extends RunRecord> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRunRecord(PathMetadata metadata) {
        super(RunRecord.class, metadata);
    }

}

