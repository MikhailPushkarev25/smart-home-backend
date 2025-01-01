BEGIN
    CREATE DATABASE smart_home;
EXCEPTION
    WHEN duplicate_object THEN
        RAISE NOTICE 'Database smart_home already exists';
END;