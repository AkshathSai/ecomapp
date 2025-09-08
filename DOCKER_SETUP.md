# Docker Hub CI/CD Setup

## Prerequisites

1. Create a Docker Hub account at https://hub.docker.com
2. Create a new repository named `ecomapp` in your Docker Hub account

## GitHub Secrets Configuration

To enable the CI/CD pipeline to push images to Docker Hub, you need to configure the following secrets in your GitHub repository:

### Required Secrets

Go to your GitHub repository → Settings → Secrets and variables → Actions → New repository secret

1. **DOCKERHUB_USERNAME**
   - Name: `DOCKERHUB_USERNAME`
   - Value: Your Docker Hub username

2. **DOCKERHUB_TOKEN**
   - Name: `DOCKERHUB_TOKEN`
   - Value: Your Docker Hub access token (not password)

### Creating Docker Hub Access Token

1. Log in to Docker Hub
2. Go to Account Settings → Security
3. Click "New Access Token"
4. Give it a descriptive name (e.g., "GitHub Actions")
5. Select appropriate permissions (Read, Write, Delete)
6. Copy the generated token and use it as `DOCKERHUB_TOKEN`

## Workflow Triggers

The CI/CD pipeline will run automatically when:

- **Push to main/develop branches**: Builds and pushes with branch name tags
- **Pull requests to main**: Runs tests only (no deployment)
- **Version tags (v*)**: Creates versioned releases (e.g., v1.0.0)
- **Manual trigger**: Can be triggered manually from GitHub Actions tab

## Image Tags

The workflow automatically creates multiple tags:
- `latest` - for main branch builds
- `main-<sha>` - for main branch with commit SHA
- `develop-<sha>` - for develop branch with commit SHA
- `v1.0.0`, `v1.0`, `v1` - for version tags
- `pr-123` - for pull request builds

## Local Testing

To test the Docker build locally:

```bash
# Build the image
docker build -t ecomapp:local .

# Run the container
docker run -p 8080:8080 ecomapp:local
```

## Multi-platform Support

The workflow builds images for both `linux/amd64` and `linux/arm64` platforms for compatibility with various deployment environments.
