-- Таблица серий наборов
CREATE SEQUENCE LEGO_SERIES_SEQ;
CREATE TABLE LEGO_SERIES
(
    LEGO_ID   BIGINT                 NOT NULL,
    LEGO_NAME CHARACTER VARYING(255) NOT NULL,
    CONSTRAINT PK_SERIES PRIMARY KEY (LEGO_ID)
);
ALTER TABLE LEGO_SERIES
    ADD CONSTRAINT UK_SERIES UNIQUE (LEGO_NAME);

-- Таблица наборов
CREATE SEQUENCE LEGO_SET_SEQ;
CREATE TABLE LEGO_SET
(
    LEGO_ID        BIGINT                 NOT NULL,
    LEGO_NAME      CHARACTER VARYING(255) NOT NULL,
    LEGO_NUMBER    CHARACTER VARYING(10)  NOT NULL,
    LEGO_YEAR      INTEGER                NOT NULL,
    LEGO_SERIES_ID BIGINT                 NOT NULL,
    CONSTRAINT PK_SET PRIMARY KEY (LEGO_ID),
    CONSTRAINT FK_SET_SERIES FOREIGN KEY(LEGO_SERIES_ID) REFERENCES LEGO_SERIES(LEGO_ID)
);
ALTER TABLE LEGO_SET
    ADD CONSTRAINT UK_SET UNIQUE (LEGO_NUMBER);
CREATE INDEX IDX_SET_SERIES ON LEGO_SET(LEGO_SERIES_ID);