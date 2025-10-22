# 1️⃣ Image de base
FROM alpine:3.21.0

# 2️⃣ Installation de Python et pip
RUN apk add --no-cache build-base libffi-dev openssl-dev py3-pip python3

# 3️⃣ Dossier de travail
WORKDIR /usr/src/app

# 4️⃣ Environnement virtuel
RUN python3 -m venv venv

# 5️⃣ On utilise le venv par défaut
ENV PATH="/usr/src/app/venv/bin:$PATH"

# 6️⃣ Installation des dépendances
COPY requirements.txt ./
RUN pip install --no-cache-dir --upgrade pip && pip install --no-cache-dir -r requirements.txt

# 7️⃣ Copie du code de l'application
COPY app.py ./
COPY templates/index.html ./templates/

# 8️⃣ Port utilisé par Flask
EXPOSE 5000

# 9️⃣ Commande de démarrage
CMD ["python", "/usr/src/app/app.py"]
