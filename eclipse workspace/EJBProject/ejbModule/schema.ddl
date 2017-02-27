
    create table Issue (
        id integer not null auto_increment,
        descr longtext,
        status varchar(255),
        Operator_id integer,
        data_id integer,
        mechanic_id integer,
        primary key (id)
    );

    create table IssueAsset (
        id integer not null auto_increment,
        descr integer not null,
        location longtext,
        primary key (id)
    );

    create table Issue_IssueAsset (
        Issue_id integer not null,
        assets_id integer not null
    );

    create table ProcessedSensorData (
        id integer not null auto_increment,
        location longtext,
        time datetime,
        track longtext,
        traincoach_id integer,
        primary key (id)
    );

    create table SEQUENCE (
        SEQ_NAME varchar(255) not null,
        SEQ_COUNT decimal(19,2),
        primary key (SEQ_NAME)
    );

    create table TrainCoach (
        id integer not null auto_increment,
        conductor longtext,
        name longtext,
        type longtext,
        primary key (id)
    );

    create table User (
        id integer not null auto_increment,
        email longtext,
        lastLogin datetime,
        name longtext,
        pass longblob,
        role varchar(255),
        salt longblob,
        primary key (id)
    );

    create table Workplace (
        id integer not null auto_increment,
        name longtext,
        primary key (id)
    );

    create table Workplace_TrainCoach (
        Workplace_id integer not null,
        traincoaches_id integer not null
    );

    create table Workplace_User (
        Workplace_id integer not null,
        mechanics_id integer not null
    );

    alter table Issue_IssueAsset 
        add constraint UK_qwosu4c6x4tq1eni0ofpvysmn unique (assets_id);

    alter table Workplace_TrainCoach 
        add constraint UK_63ow4qj7om6llrmvybvts2057 unique (traincoaches_id);

    alter table Workplace_User 
        add constraint UK_rgbn95d86i2h4qse8kvb5oryk unique (mechanics_id);

    alter table Issue 
        add constraint FK_htl3a2iv3lgvpgoxkw0yww657 
        foreign key (Operator_id) 
        references User (id);

    alter table Issue 
        add constraint FK_lwsqewu7quh1jsjc1wy4ehqm8 
        foreign key (data_id) 
        references ProcessedSensorData (id);

    alter table Issue 
        add constraint FK_g8tbwps0ma189skbcjqk0vqn4 
        foreign key (mechanic_id) 
        references User (id);

    alter table Issue_IssueAsset 
        add constraint FK_qwosu4c6x4tq1eni0ofpvysmn 
        foreign key (assets_id) 
        references IssueAsset (id);

    alter table Issue_IssueAsset 
        add constraint FK_5cmg61nctfobkg6j8rix9x6yi 
        foreign key (Issue_id) 
        references Issue (id);

    alter table ProcessedSensorData 
        add constraint FK_350ume8hyrq4rdu48vf2xihnb 
        foreign key (traincoach_id) 
        references TrainCoach (id);

    alter table Workplace_TrainCoach 
        add constraint FK_63ow4qj7om6llrmvybvts2057 
        foreign key (traincoaches_id) 
        references TrainCoach (id);

    alter table Workplace_TrainCoach 
        add constraint FK_3i2y7nttbx1ssn8s54xuf657r 
        foreign key (Workplace_id) 
        references Workplace (id);

    alter table Workplace_User 
        add constraint FK_rgbn95d86i2h4qse8kvb5oryk 
        foreign key (mechanics_id) 
        references User (id);

    alter table Workplace_User 
        add constraint FK_3sewrn6cmgt1c860hyxnd4gc7 
        foreign key (Workplace_id) 
        references Workplace (id);
