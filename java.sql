
CREATE TABLE bukkitplayers (
    playerID SERIAL PRIMARY KEY,
    playerName VARCHAR(255) NOT NULL UNIQUE,
    health DOUBLE PRECISION,
    experience DOUBLE PRECISION,
    game_mode VARCHAR(20)
);

CREATE TABLE Groups (
    groupName VARCHAR(255) PRIMARY KEY NOT NULL,
    groupPrefix VARCHAR(255) NOT NULL
);

CREATE TABLE players (
    playerID SERIAL PRIMARY KEY REFERENCES bukkitplayers(playerID),
    playerName VARCHAR(255) NOT NULL UNIQUE REFERENCES bukkitplayers(playerName),
    groupName VARCHAR(255) REFERENCES Groups(groupName),
    prefix VARCHAR(255) REFERENCES Groups(groupPrefix),
    perm BOOLEAN,
    groupAssignmentTime TIMESTAMP,
    durationMonths BIGINT,
    durationDays BIGINT,
    durationHours BIGINT,
    durationMinutes BIGINT,
    durationSeconds BIGINT
);
