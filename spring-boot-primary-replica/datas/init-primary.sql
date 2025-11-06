DO $$
BEGIN
   IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'mydb') THEN
      CREATE DATABASE mydb OWNER app_user;
END IF;
END$$;

-- 創建 replication user
CREATE ROLE repl_user WITH REPLICATION LOGIN PASSWORD 'secret';
