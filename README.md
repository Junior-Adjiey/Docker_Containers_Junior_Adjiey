# 🐳 TD 01 – Docker & DevOps

### 👨‍💻 Auteur : **Adjiey Koffi Jean-Luc Junior**

### 🎓 EFREI Paris — 2025

---

## 🎯 Objectif du TD

Ce TD m’a permis de **découvrir Docker**, d’en comprendre les **principes fondamentaux**,
et de savoir **créer, exécuter et déployer des conteneurs**.

J’ai appris à :

* Installer et tester Docker sur mon poste.
* Manipuler des images existantes comme `alpine` ou `dockersamples/static-site`.
* Créer ma **propre image Docker** à partir d’une application Flask.
* Publier une application web via un conteneur et un port réseau.
* Comprendre la différence entre une **image** (modèle) et un **conteneur** (instance).

> Ce TD illustre parfaitement la philosophie **DevOps** :
> automatiser la configuration, garantir la portabilité et simplifier le déploiement des applications.

---

## ⚙️ Étape 1 – Découverte de Docker

### 🔹 Commandes utilisées

```bash
docker run hello-world
docker pull alpine
docker images
docker run alpine ls -l
docker run alpine echo "hello from alpine"
docker run -it alpine /bin/sh
docker ps
docker ps -a
```

### 🧠 Explications

| Commande                        | Rôle                                                    |
| ------------------------------- | ------------------------------------------------------- |
| `docker run hello-world`        | Teste si Docker fonctionne correctement                 |
| `docker pull alpine`            | Télécharge l’image Alpine Linux                         |
| `docker images`                 | Liste les images disponibles localement                 |
| `docker run alpine ls -l`       | Exécute une commande dans un conteneur Alpine           |
| `docker run -it alpine /bin/sh` | Ouvre un terminal interactif à l’intérieur du conteneur |
| `docker ps`                     | Affiche les conteneurs actifs                           |
| `docker ps -a`                  | Affiche tous les conteneurs (même arrêtés)              |

---

## 🌐 Étape 2 – Déploiement d’un site statique

### 🔹 Commandes utilisées

```bash
docker run -d dockersamples/static-site
docker ps
docker stop <id>
docker rm <id>
docker run --name static-site -e AUTHOR="Jean-Luc" -d -P dockersamples/static-site
docker port static-site
docker run --name static-site-2 -e AUTHOR="Jean-Luc" -d -p 8888:80 dockersamples/static-site
```

### 🧠 Explications

| Option            | Rôle                                                                   |
| ----------------- | ---------------------------------------------------------------------- |
| `-d`              | Lance le conteneur en mode détaché (arrière-plan)                      |
| `--name`          | Donne un nom au conteneur                                              |
| `-e AUTHOR="..."` | Passe une variable d’environnement au conteneur                        |
| `-P`              | Publie automatiquement tous les ports exposés sur des ports aléatoires |
| `-p 8888:80`      | Mappe le port 80 du conteneur sur le port 8888 de la machine locale    |

➡️ Le site devient accessible sur :
`http://localhost:8888` ou `http://localhost:<port_aléatoire>`

---

## 🐍 Étape 3 – Création d’une image Flask personnalisée

### 📂 Structure du projet

```
flask-app/
│
├── app.py
├── requirements.txt
├── Dockerfile
└── templates/
    └── index.html
```

### 🔹 Fichier `app.py`

```python
from flask import Flask, render_template
import random

app = Flask(__name__)

images = [
    "https://c.tenor.com/GTcT7HODLRgAAAAM/smiling-cat-creepy-cat.gif",
    "https://media3.giphy.com/media/JIX9t2j0ZTN9S/200w.webp"
]

@app.route('/')
def index():
    url = random.choice(images)
    return render_template('index.html', url=url)

if __name__ == "__main__":
    app.run(host="0.0.0.0")
```

### 🔹 Fichier `requirements.txt`

```
Flask==3.1.0
```

### 🔹 Fichier `Dockerfile`

```dockerfile
FROM alpine:3.21.0
RUN apk add --no-cache build-base libffi-dev openssl-dev py3-pip python3
WORKDIR /usr/src/app
RUN python3 -m venv venv
ENV PATH="/usr/src/app/venv/bin:$PATH"
COPY requirements.txt ./
RUN pip install --no-cache-dir --upgrade pip && pip install -r requirements.txt
COPY app.py ./
COPY templates/index.html ./templates/
EXPOSE 5000
CMD ["python", "/usr/src/app/app.py"]
```

---

### 🔹 Commandes utilisées

```bash
docker build -t adjiey/myfirstapp .
docker images
docker run -p 8888:5000 --name myfirstapp adjiey/myfirstapp
```

### 🧠 Explications

| Commande                  | Rôle                                                                |
| ------------------------- | ------------------------------------------------------------------- |
| `docker build -t <nom>`   | Construit une image à partir du Dockerfile                          |
| `docker run -p 8888:5000` | Lance un conteneur à partir de cette image et publie les ports      |
| `FROM`                    | Définit l’image de base                                             |
| `RUN`                     | Exécute des commandes pendant la construction                       |
| `COPY`                    | Copie des fichiers dans l’image                                     |
| `ENV`                     | Définit une variable d’environnement                                |
| `EXPOSE`                  | Documente le port utilisé à l’intérieur du conteneur                |
| `CMD`                     | Définit la commande par défaut à exécuter au démarrage du conteneur |

---

## 🧱 Étape 4 – Gestion et nettoyage

```bash
docker stop myfirstapp
docker rm myfirstapp
docker rmi adjiey/myfirstapp
```

---

## 🧩 Pourquoi publier une image à l’aide d’un conteneur ?

Une **image** est un **modèle** (statique),
un **conteneur** est une **instance vivante** de cette image.

On ne peut pas “voir” ou exécuter une image tant qu’elle n’est pas lancée sous forme de conteneur.
C’est le conteneur qui fait tourner réellement l’application et la rend accessible via un port.

> 💬 En résumé :
> L’image = la recette 🍰
> Le conteneur = le gâteau préparé 🎂
> Publier = servir le gâteau sur la table (exposer le service au public).

---

## 🧠 Bilan du TD

Grâce à ce TD, j’ai appris à :

* Comprendre la différence entre **image**, **conteneur**, et **Dockerfile**.
* Construire ma propre image Docker.
* Déployer une application complète (Flask) dans un environnement isolé.
* Utiliser les options `-d`, `-P`, `-p`, `--name`, `-e` et `-it`.
* Appliquer les bonnes pratiques du **DevOps** : automatisation, reproductibilité et portabilité.

> Docker permet de garantir que **le même code fonctionne partout**,
> quelle que soit la machine ou le système d’exploitation.
