CREATE TRIGGER user_insert_tag AFTER INSERT ON tag
  FOR EACH ROW BEGIN INSERT INTO user_tag(userId,tagId,weight)
VALUES(1,NEW.tagId,0); END;

INSERT INTO tag(tagContent) VALUE ("openGL");