# TP 01 – Docker – DevOps

## **1-1. Why is it better to use the `-e` flag for environment variables instead of writing them directly in the Dockerfile?**

Because credentials such as usernames and passwords should **never be stored inside a Docker image**.
When they are written in the Dockerfile, they become visible in the image history and in any public registry.
Using the `-e` flag makes the configuration **safer** (values are passed only at runtime) and **more flexible** (you can easily change them without rebuilding the image).

---

## **1-2. Why do we need a volume attached to the PostgreSQL container?**

A volume ensures that **data persists** even when the container is removed or restarted.
Without a volume, the PostgreSQL data directory is deleted along with the container, meaning the database would lose all stored information.

---

## **1-3. Document your PostgreSQL container essentials**

**Dockerfile:**

```dockerfile
FROM postgres:17.2-alpine
ENV POSTGRES_DB=db \
    POSTGRES_USER=usr \
    POSTGRES_PASSWORD=pwd
```

**Main commands:**

```bash
docker build -t my-database .
docker network create app-network
docker run -d --name database \
  --network app-network \
  -v db-data:/var/lib/postgresql/data \
  -e POSTGRES_DB=db -e POSTGRES_USER=usr -e POSTGRES_PASSWORD=pwd \
  my-database
```

---

## **1-4. Why do we need a multi-stage build? Explain each step.**

A multi-stage build allows us to **separate the build phase from the runtime phase**, producing **lighter and cleaner images**.

**Steps:**

1. **Build stage (JDK):**
   Uses a full JDK image to compile the source code with Maven or `javac`.
2. **Runtime stage (JRE):**
   Copies the compiled JAR or `.class` files into a smaller JRE image, reducing final image size.
3. **Result:**
   The application runs efficiently without unnecessary build tools.

---

## **1-5. Why do we need a reverse proxy?**

A reverse proxy (Apache HTTPD in our case) is used to:

* Forward external HTTP requests to the backend container (`:8080`).
* Hide internal infrastructure from users.
* Simplify URLs and manage load balancing or SSL in real projects.

---

## **1-6. Why is docker-compose so important?**

Because it **orchestrates multiple containers** easily.
Instead of running each service manually with `docker run`, you can define everything in a single YAML file and start all services with:

```bash
docker compose up -d
```

It improves **automation, maintainability, and readability** of your environment.

---

## **1-7. Most useful docker-compose commands**

```bash
docker compose up -d        # Start all services
docker compose down         # Stop and remove all services
docker compose ps           # List running containers
docker compose logs -f      # View logs in real time
docker compose build        # Rebuild all images
```

---

## **1-8. Document your docker-compose.yml**

```yaml
services:
  database:
    build: ./database
    networks: [app-network]
    volumes:
      - db-data:/var/lib/postgresql/data
    environment:
      - POSTGRES_DB=db
      - POSTGRES_USER=usr
      - POSTGRES_PASSWORD=pwd

  backend:
    build: ./backend
    depends_on: [database]
    networks: [app-network]

  httpd:
    build: ./httpd
    ports:
      - "80:80"
    depends_on: [backend]
    networks: [app-network]

networks:
  app-network:

volumes:
  db-data:
```

---

## **1-9. Document your publication commands**

```bash
docker login
docker tag my-database adjiey/my-database:1.0
docker push adjiey/my-database:1.0
```

You should repeat this for each image: backend, database, and httpd.

---

## **1-10. Why do we put our images into an online repo?**

Because DockerHub (or any image registry) allows us to:

* **Share** images with teammates or servers.
* **Automate** deployments (Ansible, CI/CD).
* **Version** images and roll back easily if needed.

---

# TP 02 – GitHub Actions – DevOps

## **2-1. What are Testcontainers?**

**Testcontainers** are Java libraries that automatically run temporary Docker containers for testing.
They let integration tests use real services (like PostgreSQL) instead of mocks, ensuring the tests are realistic and isolated.

---

## **2-2. Why do we need to use secured variables (secrets)?**

Because sensitive credentials (like DockerHub tokens or SonarCloud keys) must **not appear in code or logs**.
GitHub Secrets encrypt them and make them available safely during workflow execution.

---

## **2-3. Why did we add `needs: test-backend` to the build-and-push job?**

This ensures the Docker build and push steps only run **after the tests have passed**.
Without this dependency, the pipeline might push broken or untested code.

---

## **2-4. Why do we push Docker images?**

To make the latest application versions **available for deployment**.
Once pushed, the images can be pulled by Ansible, servers, or teammates anywhere, ensuring consistent environments.

---

## **2-5. What is SonarCloud used for?**

SonarCloud provides a **Quality Gate** — it checks code quality, detects bugs, vulnerabilities, and code smells.
It guarantees that only clean, maintainable, and secure code reaches the main branch.

---

# TP 03 – Ansible – DevOps

## **3-1. Document your inventory and base commands**

**Inventory (ansible/inventories/setup.yml):**

```yaml
all:
  vars:
    ansible_user: admin
    ansible_ssh_private_key_file: ~/.ssh/id_rsa
  children:
    prod:
      hosts:
        koffi-jean-luc-junior.adjiey.takima.cloud:
```

**Main commands:**

```bash
ansible all -i inventories/setup.yml -m ping
ansible all -i inventories/setup.yml -m setup -a "filter=ansible_distribution*"
ansible all -i inventories/setup.yml -m apt -a "name=apache2 state=absent" --become
```

---

## **3-2. Document your playbook**

**playbook.yml**

```yaml
- hosts: all
  gather_facts: true
  become: true

  roles:
    - docker
    - network
    - database
    - backend
    - proxy
```

Each role has a dedicated task file (`roles/<role_name>/tasks/main.yml`) that installs Docker, creates a network, and launches containers using the `docker_container` module.

---

## **3-3. Document your docker_container tasks configuration**

Example for the backend:

```yaml
- name: Run backend container
  docker_container:
    name: backend
    image: adjiey/simple-api:1.0
    networks:
      - name: app-network
    env:
      SPRING_DATASOURCE_URL: jdbc:postgresql://database:5432/db
      SPRING_DATASOURCE_USERNAME: usr
      SPRING_DATASOURCE_PASSWORD: pwd
```

---

## **3-4. Is it safe to deploy automatically every time a new image is pushed?**

Not entirely.
Automatic deployments can push **unstable or compromised images** if something goes wrong in the CI pipeline.
To make it safer, we can:

* Require **manual approval** before deployment.
* Deploy only **tagged or versioned releases**.
* Add **security scans** and **rollback mechanisms**.

---

Would you like me to add a short **introduction + summary paragraph** at the top (to make your README more professional)?
