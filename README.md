# API Documentation — Issue Management System (IMS)

All protected routes require a valid **JWT token**.

---

## Auth API

### Base Path: `/api/auth`

| Endpoint       | Method | Access | Description              | Response                           |
|----------------|--------|--------|--------------------------|-------------------------------------|
| `/register`    | POST   | Public | Register a new user      | `UserResponse` (created user info)  |
| `/login`       | POST   | Public | Login and get JWT token  | `{ message, status, token }`        |

---

## Posts API

### Base Path: `/api/posts`

| Endpoint                 | Method | Access               | Description                     | Response              |
|--------------------------|--------|----------------------|---------------------------------|------------------------|
| `/`                      | POST   | User/Admin           | Create a new post               | `PostResponse`         |
| `/{id}/submit`           | PUT    | User/Admin           | Submit a post for approval      | `PostResponse`         |
| `/{id}/approve`          | PUT    | Admin                | Approve a post                  | `PostResponse`         |
| `/{id}/reject`           | PUT    | Admin                | Reject a post                   | `PostResponse`         |
| `/{id}/close`            | PUT    | Admin                | Close a post                    | `PostResponse`         |
| `/{id}/assign-update`    | PUT    | Admin                | Assign update to a post         | `PostResponse`         |
| `/`                      | GET    | Admin                | Get all posts                   | `List<PostResponse>`   |
| `/approved`              | GET    | User/Admin           | Get approved posts              | `List<PostResponse>`   |
| `/user/posts`            | GET    | User                 | Get posts by logged-in user     | `List<PostResponse>`   |

---

## Comments API

### Base Path: `/api/posts/{postId}/comments`

| Endpoint | Method | Access      | Description              | Response                 |
|----------|--------|-------------|--------------------------|---------------------------|
| `/`      | POST   | User/Admin  | Add a comment to a post  | `CommentResponse`         |
| `/`      | GET    | User/Admin  | Get comments for a post  | `List<CommentResponse>`   |

