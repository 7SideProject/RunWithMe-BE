package com.runwithme.runwithme.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 176546598L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final com.runwithme.runwithme.global.entity.QBaseEntity _super = new com.runwithme.runwithme.global.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final StringPath createMember = _super.createMember;

    public final StringPath email = createString("email");

    public final NumberPath<Integer> height = createNumber("height", Integer.class);

    public final com.runwithme.runwithme.global.entity.QImage image;

    //inherited
    public final EnumPath<com.runwithme.runwithme.global.entity.DeleteState> isDeleted = _super.isDeleted;

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    //inherited
    public final StringPath updateMember = _super.updateMember;

    public final NumberPath<Integer> weight = createNumber("weight", Integer.class);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.image = inits.isInitialized("image") ? new com.runwithme.runwithme.global.entity.QImage(forProperty("image")) : null;
    }

}

