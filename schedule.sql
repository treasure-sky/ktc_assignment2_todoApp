-- todo 생성 쿼리
CREATE TABLE todo
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    content     VARCHAR(255) NOT NULL,
    writer_name VARCHAR(10)  NOT NULL, -- 삭제됨
    password    VARCHAR(20)  NOT NULL,
    created_at  DATETIME     NOT NULL,
    updated_at  DATETIME     NOT NULL
);
-- writer_id FK 추가됨

-- writer 생성 쿼리
CREATE TABLE writer
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(10) NOT NULL,
    email      VARCHAR(50) NOT NULL UNIQUE,
    created_at DATETIME    NOT NULL,
    updated_at DATETIME    NOT NULL
);

-- writer 테이블로 이동 과정

-- 1. 임의 이메일을 넣어서 todo에 존재하는 이름을 가지는 writer 생성
INSERT INTO writer (name, email, created_at, updated_at)
SELECT DISTINCT writer_name                         as name,
                CONCAT(writer_name, '@example.com') as email,
                NOW()                               as created_at,
                NOW()                               as updated_at
FROM todo;

-- 2. todo에 writer와 연관관계를 설정할 FK 생성
ALTER TABLE todo
    ADD COLUMN writer_id BIGINT;

-- 3. todo의 writer_name과 일치하는 writer의 id를 찾아서 writer_id에 설정
UPDATE todo t
    JOIN writer w ON t.writer_name = w.name
SET t.writer_id = w.id;

-- 4. writer_id를 NOT NULL로 변경
ALTER TABLE todo
    MODIFY COLUMN writer_id BIGINT NOT NULL;

-- 5. 제약조건 설정
ALTER TABLE todo
    ADD FOREIGN KEY (writer_id) REFERENCES writer (id);

-- 6. 기존 todo 테이블에 필요없어진 칼럼 삭제
ALTER TABLE todo
    DROP COLUMN writer_name;