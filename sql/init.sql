-- 0. 기존 DB 삭제 (주의: 데이터 전부 삭제됨)
DROP DATABASE IF EXISTS emergency_system;

-- 1. 데이터베이스 생성 및 사용
CREATE DATABASE IF NOT EXISTS emergency_system;
USE emergency_system;

-- 2. 사용자 정보 테이블 생성
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL
);

-- 3. 로그인 가능한 예시 사용자 추가
INSERT INTO users (username, password, name) VALUES
('jason0153', '1234', '이지성'),
('test1', '1111', '김철수'),
('test2', '2222', '이민수');



-- 1. 데이터베이스 생성 및 사용
CREATE DATABASE IF NOT EXISTS emergency_system;
USE emergency_system;

-- 2. 기존 신고 데이터 완전 삭제 및 ID 초기화
-- TRUNCATE TABLE reports;

-- 3. 신고 정보 테이블 생성 (최초 1회만 실행됨)
CREATE TABLE IF NOT EXISTS reports (
    id INT AUTO_INCREMENT PRIMARY KEY,
    report_time DATETIME NOT NULL,
    address VARCHAR(100) NOT NULL,
    accident_type VARCHAR(50) NOT NULL,
    urgency_level VARCHAR(10) NOT NULL,
    location_detail VARCHAR(100),
    problem_description VARCHAR(100),
    witness_location VARCHAR(100),
    false_report VARCHAR(100),
    phone_number VARCHAR(20)
);

-- 4. 예시 신고 데이터 삽입
INSERT INTO reports 
(report_time, address, accident_type, urgency_level, location_detail, problem_description, witness_location, false_report, phone_number)
VALUES
/*
('0000-01-01 00:00:00, '시(도) 구 동, '사고종류', 긴급도. 주소다시, 상세사고, 상세 위치, 허위신고, 신고자 번호)
*/
('2025-04-24 17:46:14', '서울특별시 용산구 서빙고동', '대물사고(구조)', '중', '서울특별시 용산구 서빙고동', '차량 파손', '구급차 출발', '651e464d68953_20250424.wav', '010-1234-5678'),
('2025-04-25 11:23:58', '경기도 수원시 팔달구 인계동', '일반화재(화재)', '상', '경기도 수원시 팔달구', '전기 누전 화재', '인계동 먹자골목', '없음', '010-2345-6789'),
('2025-04-25 13:12:42', '경기도 고양시 일산동구 백석동', '심정지 환자', '상', '고양시 일산동구', '심정지 환자', '백석역', '없음', '010-3456-7890'),
('2025-04-25 15:50:06', '서울특별시 송파구 잠실본동', '기타(기타)', '하', '송파구 잠실본동', '의심 물체', '어린이공원 입구', '중간', '010-4567-8901'),
('2025-04-25 16:08:21', '경기도 성남시 분당구 서현동', '질병(구급)', '중', '분당구 서현동', '고열 환자', '서현역 앞 카페', '없음', '010-5678-9012');


SELECT * FROM reports;

SHOW VARIABLES LIKE 'datadir';



-- 컬럼 추가 (이미 존재하지 않을 경우에만 실행해야 함)
ALTER TABLE reports
ADD COLUMN dialogue_path VARCHAR(255),
ADD COLUMN summary_path VARCHAR(255);

UPDATE reports
SET 
    dialogue_path = '/Data/dialogue.txt',
    summary_path = '/Data/summary.txt'
WHERE id = 1;

ALTER TABLE reports ADD COLUMN memo TEXT;
