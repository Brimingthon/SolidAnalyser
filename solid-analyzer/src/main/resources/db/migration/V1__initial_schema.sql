CREATE TABLE metrics (
    id SERIAL PRIMARY KEY,
    class_count INTEGER NOT NULL,
    method_count INTEGER NOT NULL,
    cyclomatic_complexity INTEGER NOT NULL,
    cbo INTEGER NOT NULL
);

CREATE TABLE files (
    id SERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    analysis_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    metrics_id INTEGER NOT NULL,
    CONSTRAINT fk_metrics FOREIGN KEY (metrics_id) REFERENCES metrics(id)
);

CREATE TABLE violations (
    id SERIAL PRIMARY KEY,
    file_id INTEGER NOT NULL,
    principle VARCHAR(10) NOT NULL,
    description TEXT NOT NULL,
    CONSTRAINT fk_files FOREIGN KEY (file_id) REFERENCES files(id)
);
