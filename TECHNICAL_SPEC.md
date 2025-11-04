## პროექტის მიმოხილვა

### 📌 პროექტის დასახელება

**Softgen — ბანერების მართვის პლატფორმა (Admin/User როლებით, JWT, Projects/Banners, A/B ტესტირება, Scheduler Engine)**

### 🎯 მიზანი

ვებაპლიკაციის შექმნა, რომელიც უზრუნველყოფს:
- ✅ მომხმარებელთა მართვასა და აქტივაციას (Admin → Invite, User → Activation)
- ✅ უსაფრთხო ავტორიზაციას JWT-ზე დაფუძნებით
- ✅ პროექტებისა და ბანერების სრულ CRUD-ს
- ✅ A/B ტესტირებას CTR/Conversion-ის მიხედვით
- ✅ ბანერების დაგეგმვასა და პრიორიტეტულ ჩვენებას (Scheduler Engine)
- ✅ პაროლის აღდგენის სრულ ნაკადს

პლატფორმა განკუთვნილია მარკეტინგული ბანერების შექმნის, დისტრიბუციისა და ოპტიმიზაციის ავტომატიზაციისთვის.

### 📏 პროექტის მასშტაბი

| პარამეტრი | დეტალები |
| --- | --- |
| 👥 მომხმარებლები | 10-5,000+ (User/Admin) |
| 🖥️ დეპლოიმენტი | შიდა სერვერი ან Cloud (Docker/K8s-ready) |
| 📱 პლატფორმა | Web Application (Desktop & Mobile Responsive) |

---

## ⚙️ ფუნქციონალური მოთხოვნები

### 🔐 ავტორიზაცია და ანგარიშები

#### 🔑 Admin საწყისი რეგისტრაცია (ერთჯერადი)
- POST `/api/v1/admin/auth/signup` — თუ ადმინი არსებობს → 409
- Email უნიკალურია; პაროლი ძლიერი (min 8, რთული regex)

#### 👤 User შექმნა და აქტივაცია
- POST `/api/v1/admin/users` — Admin ქმნის, იგზავნება `activationToken`
- POST `/api/v1/auth/activate` — User აყენებს პაროლს ტოკენით (single-use, expirable)

#### 🔓 Login/Logout/Me
- POST `/api/v1/auth/login` — JWT ტოკენი + `expires_in`
- GET `/api/v1/users/me` — პროფილი JWT-ს საფუძველზე
- POST `/api/v1/auth/logout` — ტოკენის გაუქმება (blacklist თუ ჩართულია)

#### 🔄 Password Reset
- POST `/api/v1/auth/forgot-password` — resetToken გაგზავნა email-ზე
- POST `/api/v1/auth/reset-password` — ახალი პაროლი resetToken-ით (single-use, expirable)

### 📁 User Panel — Projects CRUD
- GET `/api/v1/projects` — საკუთარი პროექტების სია + ფილტრაცია/პაგინაცია
- POST `/api/v1/projects` — ახალი პროექტის შექმნა
- GET `/api/v1/projects/{projectId}` — პროექტის ნახვა (ownership check)
- PUT/PATCH `/api/v1/projects/{projectId}` — სრული/ნაწილობრივი განახლება
- DELETE `/api/v1/projects/{projectId}` — Soft delete რეკომენდებულია

### 🖼️ User Panel — Banners CRUD
- GET `/api/v1/projects/{projectId}/banners` — სია
- POST `/api/v1/projects/{projectId}/banners` — შექმნა (title, imageUrl, targetUrl, notes)
- GET `/api/v1/projects/{projectId}/banners/{bannerId}` — ნახვა
- PUT/PATCH `/api/v1/projects/{projectId}/banners/{bannerId}` — განახლება
- DELETE `/api/v1/projects/{projectId}/banners/{bannerId}` — წაშლა/არქივაცია

### 🧑‍💼 Admin Panel
- Users: GET, GET/{id}, PATCH/{id}, DELETE/{id}
- Projects: GET, GET/{id}, PATCH/{id}, DELETE/{id}
- Banners: GET, GET/{id}, PATCH/{id}, DELETE/{id}

### 🧪 A/B ტესტირება
- მოდელის ველები: `variant_group, impressions, clicks, conversions, ctr, conversion_rate`
- CTR/Conversion Rate გამოთვლა `ABTestingService`-ში
- ტესტის დასრულებისას — დაბალი CTR/CR ბანერები პაუზაში, გამარჯვებული აქტიური
- Report: GET `/api/v1/admin/abtesting/report`

### ⏱️ Scheduler Engine
- მოდელის ველები: `start_time, end_time, priority`
- `BannerSchedulerService` ააქტიურებს/აჩერებს დროის მიხედვით
- პრიორიტეტული ჩვენება: მაღალი პრიორიტეტი → უფრო ხშირი იმპრესიები
- სტატუსის მონიტორინგი: GET `/api/v1/admin/scheduler/status`

---

## 👤 მომხმარებლის როლები

| როლი | აღწერა | წვდომა | უფლებები |
| --- | --- | --- | --- |
| 👨‍💼 Admin | სისტემის ადმინისტრატორი | სრული | მომხმარებლები, ყველა პროექტი/ბანერი, სტატუსები, რეპორტები, Scheduler |
| 👤 User | პლატფორმის მომხმარებელი | შეზღუდული | საკუთარი პროექტები და ბანერები, HTML გვერდების გენერაცია |

RBAC enforce-დება API დონეზე (Spring Security) და ფრონტზე (ProtectedRoute, `requireRole`).

---

## 📊 Dashboard (მთავარი)

- User:
  - საკუთარი Projects/Banners quick stats
  - ბოლო 5 პროექტი, ბოლო აქტივობები
- Admin:
  - Total Users/Projects/Banners, Pending/Approved სტატუსები
  - სწრაფი ბმულები მართვის გვერდებზე

---

## 🔌 API Endpoints (რეფერენსი)

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| POST | `/api/v1/admin/auth/signup` | Public | ერთი-ჯერადი Admin შექმნა |
| POST | `/api/v1/admin/users` | Admin | User invite + activationToken |
| POST | `/api/v1/auth/activate` | Public | ანგარიშის აქტივაცია |
| POST | `/api/v1/auth/login` | Public | ავტორიზაცია JWT-ით |
| GET | `/api/v1/users/me` | User/Admin | საკუთარი პროფილი |
| POST | `/api/v1/auth/logout` | User/Admin | Logout |
| POST | `/api/v1/auth/forgot-password` | Public | resetToken email-ზე |
| POST | `/api/v1/auth/reset-password` | Public | პაროლის აღდგენა |
| GET | `/api/v1/projects` | User | პროექტების სია |
| POST | `/api/v1/projects` | User | პროექტის შექმნა |
| GET | `/api/v1/projects/{projectId}` | User | ნახვა (ownership) |
| PUT/PATCH | `/api/v1/projects/{projectId}` | User | განახლება |
| DELETE | `/api/v1/projects/{projectId}` | User | soft delete |
| GET | `/api/v1/projects/{projectId}/banners` | User | ბანერების სია |
| POST | `/api/v1/projects/{projectId}/banners` | User | ბანერის შექმნა |
| GET | `/api/v1/projects/{projectId}/banners/{bannerId}` | User | ბანერის ნახვა |
| PUT/PATCH | `/api/v1/projects/{projectId}/banners/{bannerId}` | User | განახლება |
| DELETE | `/api/v1/projects/{projectId}/banners/{bannerId}` | User | წაშლა |
| GET | `/api/v1/admin/users` | Admin | Users list |
| GET | `/api/v1/admin/users/{userId}` | Admin | User detail |
| PATCH | `/api/v1/admin/users/{userId}` | Admin | განახლება |
| DELETE | `/api/v1/admin/users/{userId}` | Admin | დეაქტივაცია/soft delete |
| GET | `/api/v1/admin/projects` | Admin | Projects list |
| GET | `/api/v1/admin/projects/{projectId}` | Admin | Project detail |
| PATCH | `/api/v1/admin/projects/{projectId}` | Admin | სტატუსი |
| DELETE | `/api/v1/admin/projects/{projectId}` | Admin | არქივაცია/წაშლა |
| GET | `/api/v1/admin/banners` | Admin | Banners list |
| GET | `/api/v1/admin/banners/{bannerId}` | Admin | Banner detail |
| PATCH | `/api/v1/admin/banners/{bannerId}` | Admin | სტატუსი |
| DELETE | `/api/v1/admin/banners/{bannerId}` | Admin | არქივაცია/წაშლა |
| GET | `/api/v1/admin/abtesting/report` | Admin | A/B report |
| GET | `/api/v1/admin/scheduler/status` | Admin | Scheduler სტატუსი |

---

## 🏗️ ტექნიკური არქიტექტურა

### 🔧 Backend Stack (Spring Boot)
- Spring Web, Spring Security, Spring Data JPA, Validation
- JavaMailSender (SMTP), Lombok
- Database: MySQL 8+
- Background jobs: Spring @Scheduled ან Quartz (Scheduler Engine, A/B optimizations)
- Token blacklist / Rate Limiting: Redis (ოპციონალური)
- Migration: Flyway

### 🎨 Frontend Stack (React)
- React 19, Vite
- Tailwind CSS, shadcn UI, Framer Motion
- React Router v7
- Axios (interceptors, API layer)
- HTML Generator utility (ბანერების გვერდები)

### 🚀 DevOps & Infra
- Profiles: `dev`, `prod` (Spring), `.env` ფაილები (FE)
- CI/CD: GitHub Actions/GitLab CI (lint/test/build/deploy)
- Containerization: Docker (+ Compose) — app + db (+ redis)
- Monitoring: Actuator/metrics/logs; optional Sentry/APM

---

## 🗄️ მონაცემთა მოდელი (შედეგის მისაღებად საკმარისი მინიმუმი)

### User
- `id, name, surname, email (unique), password_hash, role (ADMIN|USER), status (pending|active|deactivated)`
- Activation: `activation_token, activation_expires_at`
- Reset: `reset_token, reset_expires_at, reset_used`
- `created_at, updated_at`

### Project
- `id, user_id (FK), name, surname, project_name, description, status (draft|active|archived), soft_deleted`
- `created_at, updated_at`

### Banner
- `id, project_id (FK), title, image_url, target_url, notes, status (pending|approved|rejected|active|paused|archived)`
- A/B: `variant_group, impressions, clicks, conversions, ctr, conversion_rate`
- Schedule: `start_time, end_time, priority`
- `created_at, updated_at`

ინდექსები: `users.email`, `projects(user_id, status)`, `banners(project_id, status)`, დროით ფილტრებზე შესაბამისი ინდექსები.

---

## 🔒 უსაფრთხოება

- JWT:
  - მოკლე ვადა access token-ზე (15–60 წთ), სურვილისამებრ refresh (ფაზა 2)
  - Authorization: Bearer header; Logout → blacklist (Redis) თუ ჩართულია
- RBAC:
  - Endpoint-level დაცვა (ADMIN/USER) + ownership checks
- Validation:
  - DTO constraints, 400/422 სტანდარტული შეცდომის ფორმატი
- Tokens:
  - Activation/Reset single-use, expirable, სურვილისამებრ ჰეშირებული შენახვა
- Rate limiting:
  - login/forgot-password → Bucket4j/Redis
- CORS/HTTPS/Security headers, SQLi/XSS პრევენცია, audit logging კრიტიკულ ქმედებებზე

---

## ⚡ Performance Optimization

### Backend
- Pagination ყველა list endpoint-ზე
- DTO projections მძიმე ობიექტების ნაცვლად
- Indexing სტრატეგია (ზემოთ)
- Async email გაგზავნა
- არჩევითი ქეშირება (Caffeine/Redis): user projects list, admin stats
- Batch updates A/B ოპტიმიზაციაში

### Frontend
- Route-level code splitting
- Debounced search/filter
- Client caching (სურვილისამებრ React Query/SWR შემდეგ ფაზაში)
- Image lazy loading, skeleton loaders

### Scheduler/A/B
- Fixed-rate jobs, UTC დრო, Clock abstraction ტესტირებისთვის
- იდემპოტენტური ოპერაციები, აგრეგაცია window-ებით

---

## 🧪 Testing Strategy

### Backend
- Unit: services (Auth, Projects, Banners, ABTestingService, BannerSchedulerService)
- Integration: controllers + DB (Testcontainers for MySQL)
- Security: role/access matrix, ownership checks
- Email/token flows: expiration/single-use ტესტები
- Metrics correctness: CTR/Conversion გამოთვლა

### Frontend
- Unit: UI utils/components
- Integration: გვერდები API mock-ებით (MSW)
- E2E: Cypress/Playwright — auth და CRUD ძირითადი ნაკადები, როლებზე წვდომა

### Non-functional
- Load tests (k6/JMeter) — lists endpoints, auth bursts
- Vulnerability scan (OWASP ZAP), dependency audit

---

## 🚀 Deployment

- Docker Compose სერვისები: backend, frontend, mysql, redis (არჩევით), nginx (არჩევით)
- Envs: JWT_SECRET, DB creds, SMTP creds, CORS origins
- Flyway migrations on startup
- Actuator health checks; rolling deploy (თუ კონტეინერიზებულია)

### Production Checklist
- [ ] DEBUG off, ძლიერი JWT secret/keys
- [ ] HTTPS/TLS, CORS სწორად გამართული
- [ ] DB migrations და ინდექსები
- [ ] SMTP გაწყობა და ტესტი
- [ ] ლოგირება/მონიტორინგი/ალერტები

---

## 📈 Monitoring & Logging  

- Structured logs (JSON) + request-id/trace-id
- Metrics: latency, throughput, error rate, job runtimes
- Health: `/actuator/health`, readiness/liveness
- Alerts: auth failure spikes, scheduler lag, email queue failures

---

## 🗓️ Project Timeline & Milestones

### Phase 1: Foundation (BE/FE setup)
- Spring Boot პროექტის ინიციალიზაცია, MySQL კონფიგი, უსაფრთხოება
- React/Vite/Tailwind/shadcn სეტაპი, Auth flow (login, activation)

### Phase 2: Core User Features
- Projects CRUD (FE პేజეები + BE endpoints)
- Banners CRUD, HTML generator (preview, download)
- Dashboard (User/Admin ძირითადი სტატისტიკები)

### Phase 3: Admin Panel
- Users/Projects/Banners მართვა (ცხრილები, ფილტრები, სტატუსები)

### Phase 4: Password Reset Flow
- Forgot/Reset (FE + BE)

### Phase 5: A/B Testing & Scheduler Engine
- ABTestingService + Report endpoint
- BannerSchedulerService + status endpoint

### Phase 6: QA, Hardening, Deployment
- ტესტირება, ოპტიმიზაცია, მონიტორინგი, პროდ-დეპლოი

---

## 🎓 Acceptance Criteria (ამონარიდი)
- Admin signup: 201 ან 409 თუ უკვე არსებობს
- User invite: იგენერირება activationToken და იგზავნება email
- Activation: status `pending→active`, single-use token
- Ownership: User ხედავს/ცვლის მხოლოდ საკუთარ რესურსებს
- A/B: Report აჩვენებს CTR/CR-ს; loser ბანერები პაუზაში
- Scheduler: აქტივაცია/დეაქტივაცია სწორ დროსა და პრიორიტეტით
- FE: Create Project wizard → პირველი ბანერი → HTML გენერაცია/გადმოწერა

---

## 📚 Documentation & Artifacts
- OpenAPI 3.0 სპეკი (`/api/v1`) — source of truth FE/BE-სთვის
- ER დიაგრამა (User/Project/Banner)
- README: setup/run/dev, envs, SMTP

---

## ⚠️ რისკები და მიტიგაცია

| რისკი | ალბათობა | გავლენა | მიტიგაცია |
| --- | --- | --- | --- |
| მოთხოვნების ცვლილება | საშუალო | მაღალი | API versioning, OpenAPI source of truth |
| უსაფრთხოება (token leakage) | დაბალი/საშუალო | მაღალი | მოკლე ვადები, blacklist, httpOnly cookie ფაზა 2 |
| Email მიწოდების პრობლემები | საშუალო | საშუალო | Retry/backoff, queue, observability |
| Scheduler/A/B სიზუსტე | საშუალო | საშუალო | UTC, Clock abstraction, batch ოპერაციები |
| DB შესრულება | დაბალი | მაღალი | ინდექსები, pagination, caching |
| FE/BE დესინქრონიზაცია | საშუალო | საშუალო | კონტრაქტული ტესტები, MSW mocks |

## 📞 დასკვნა

Softgen უზრუნველყოფს უსაფრთხო, სტაბილურ და გაფართოებად პლატფერმას ბანერების მართვისთვის. MVP ფაზა ფოკუსირდება Auth → Projects/Banners → HTML Generator → Admin მართვაზე, ხოლო A/B ტესტირება და Scheduler Engine ზრდის შედეგიანობას ავტომატური ოპტიმიზაციით. არქიტექტურა მზადაა სკალირებისა და მომავალში ფუნქციონალის გაღრმავებისთვის.

**დოკუმენტის ვერსია:** 1.0  
**ბოლო განახლება:** 2025-11-04  
**სტატუსი:** მზად განხორციელებისთვის

---

## 📦 Frontend Deliverables (Route & Service Summary)

### Routes (required)
- `/` Home (DONE)
- `/logIn` Login (DONE)
- `/complete-signup` Activation (DONE)
- `/admin/create-user` Admin invite (DONE)
- `/Panel` Dashboard (exists; needs stats)
- `/projects` Projects list (NEW)
- `/project` Create project (exists; needs form/wizard)
- `/project/:name/:id` Project detail/edit (exists; needs implementation)
- `/allUsers` Admin users (NEW)
- `/AllProjects` Admin projects (NEW)
- `/admin/banners` Admin banners (NEW, optional)
- `/forgot-password`, `/reset-password` Password reset (NEW)

### Services (required)
- `auth.service.js` (DONE)
- `project.service.js`: list/get/create/put/patch/delete
- `banner.service.js`: list/get/create/put/patch/delete (scoped by project)
- `admin.service.js`: users/projects/banners admin endpoints
- `html.service.js` (or `lib/html-generator.js`): `generateBannerHTML`, `downloadHTMLFile`, preview support

### HTML Generator (required)
- თითო ბანერზე გენერირებული HTML გვერდი: image + link, responsive, copy/download, preview.

---

## 📐 Non-Functional Targets (SLO/SLA)
- Availability: ≥ 99.5%
- API latency: p95 ≤ 300ms (GET), ≤ 600ms (write)
- Pagination: default 20, max 100
- Rate limiting: login ≤ 5/15min/ip; forgot-password ≤ 3/hour/email

### API Versioning Policy
- Stable at `/api/v1`; breaking changes მხოლოდ `/v2`-ში. Deprecation window: 90 დღე.

### Error Response Schema (ერთიანი ფორმატი)
```json
{ "code": "string", "message": "human readable", "details": {"field":"error"}, "trace_id": "uuid" }
```
HTTP mapping: 400 validation, 401 auth, 403 forbidden, 404 not found, 409 conflict, 410 gone (expired token), 422 unprocessable, 429 rate limit, 500 server.

---

## 🔄 State & Transitions

### Project
- States: `draft → active → archived`
- Transitions: User: draft→active, active→archived; Admin: ნებისმიერი transition.

### Banner
- States: `pending → approved|rejected`, `approved → active|paused → archived`, `rejected → archived`
- Transitions: User: edit pending/active/paused within own project; Admin: approve/reject, archive.

---

## 🧪 A/B Testing Policy (Operational)
- Distribution: default 50/50 (A/B); მხარდაჭერა A/B/C თანაბრად.
- Test window: 7 დღე ან ≥ 1,000 impressions/variant (რომელი קודם).
- Metrics: CTR = clicks/impressions; Conversion Rate = conversions/impressions.
- Winner: უმაღლესი CTR; ტოლობისას — მეტი impressions; loser ბანერები → paused.
- Reporting: GET `/api/v1/admin/abtesting/report` aggregates by `variant_group`.

---

## ⏱️ Scheduler Engine Policy
- Tick interval: ყოველ 1 წუთში; დრო: UTC.
- Activation rule: `now >= start_time` და სტატუსი არა აქტიური → activate.
- Expiry rule: `now > end_time` → stop/archive per policy.
- Priority distribution: weighted round-robin (მაღალი პრიორიტეტი უფრო ხშირად ირჩევა).

---

## 🔐 Token Storage & Security Notes
- Phase 1: Bearer JWT local storage + header, ლოგაუთი კლიენტის მხარეს (არჩევითი ბლექლისტი ბექენდის მხარეს).
- Phase 2 (გაუმჯობესება): httpOnly cookie + refresh flow, მოკლევადიანი access tokens.
- CORS allowlist env-ით; CSP headers რეკომენდებულია.

---

## 📊 Monitoring KPIs
- Login error rate < 1%
- Scheduler lag < 60s
- Email delivery failure < 2%
- DB connection pool saturation < 80%
