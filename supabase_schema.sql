-- ============================================================
-- AgriSuivi – Schéma Supabase PostgreSQL
-- Exécuter dans le SQL Editor de votre projet Supabase
-- ============================================================

CREATE TABLE IF NOT EXISTS cycles_culture (
    id                     UUID    PRIMARY KEY DEFAULT gen_random_uuid(),
    variete                TEXT    NOT NULL,
    numero_parcelle        TEXT    NOT NULL,
    date_semis             DATE    NOT NULL,
    duree_croissance_jours INTEGER NOT NULL CHECK (duree_croissance_jours > 0),
    photo_url              TEXT,
    statut                 TEXT    NOT NULL DEFAULT 'EN_COURS'
                               CHECK (statut IN ('EN_COURS', 'RECOLTEE', 'ECHEC')),
    notes                  TEXT    NOT NULL DEFAULT '',
    created_at             BIGINT  NOT NULL DEFAULT (EXTRACT(EPOCH FROM now()) * 1000)::BIGINT
);

CREATE TABLE IF NOT EXISTS photos_suivi (
    id               UUID   PRIMARY KEY DEFAULT gen_random_uuid(),
    cycle_culture_id UUID   NOT NULL REFERENCES cycles_culture(id) ON DELETE CASCADE,
    photo_url        TEXT   NOT NULL,
    description      TEXT   NOT NULL DEFAULT '',
    signale_parasite BOOLEAN NOT NULL DEFAULT FALSE,
    created_at       BIGINT  NOT NULL DEFAULT (EXTRACT(EPOCH FROM now()) * 1000)::BIGINT
);

CREATE INDEX IF NOT EXISTS idx_photos_cycle_id ON photos_suivi(cycle_culture_id);

-- RLS (accès complet clé anon – adapter si auth multi-utilisateurs)
ALTER TABLE cycles_culture ENABLE ROW LEVEL SECURITY;
ALTER TABLE photos_suivi   ENABLE ROW LEVEL SECURITY;
CREATE POLICY "allow_all_cycles" ON cycles_culture FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "allow_all_photos" ON photos_suivi   FOR ALL USING (true) WITH CHECK (true);

-- Storage bucket : créer manuellement via Dashboard
-- Nom : "cultures"   |   Accès public : OUI
