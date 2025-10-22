# Étape 1 — Image de base
FROM postgres:17.2-alpine

# Étape 3 — Copie des scripts SQL d’initialisation
COPY ./initdb/ /docker-entrypoint-initdb.d/
