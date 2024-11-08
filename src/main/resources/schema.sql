DROP TABLE IF EXISTS regions;
CREATE TABLE regions (
                         id INTEGER PRIMARY KEY,
                         name VARCHAR(255) NOT NULL
);

INSERT INTO regions (id, name) VALUES
                                   (1, '서울'),
                                   (2, '인천'),
                                   (3, '대전'),
                                   (4, '대구'),
                                   (5, '광주'),
                                   (6, '부산'),
                                   (7, '울산'),
                                   (8, '세종'),
                                   (31, '경기'),
                                   (32, '강원'),
                                   (33, '충북'),
                                   (34, '충남'),
                                   (35, '경북'),
                                   (36, '경남'),
                                   (37, '전북'),
                                   (38, '전남'),
                                   (39, '제주');