ALTER TABLE halls
ADD CONSTRAINT uk_unique_arena_id_and_hall_name UNIQUE (name, arena_id);