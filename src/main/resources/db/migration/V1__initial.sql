CREATE TABLE cloths (
    id varchar(36) NOT NULL,
    name text NOT NULL,
    vendorCode text NOT NULL,
    CONSTRAINT foo_pkey PRIMARY KEY (id)
);
COMMENT ON TABLE cloths IS 'Одежда';
COMMENT ON COLUMN cloths.id IS 'id';
COMMENT ON COLUMN cloths.name IS 'Название одежды';
COMMENT ON COLUMN cloths.vendorCode IS 'Артикул';

