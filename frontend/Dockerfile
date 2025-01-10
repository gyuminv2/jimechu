# Step 1: Base image
FROM node:18 as build

# Step 2: Working directory 설정
WORKDIR /app

# Step 3: Package.json 복사 및 의존성 설치
COPY package.json ./
RUN npm install

# Step 4: 소스 코드 복사 및 빌드
COPY . ./
RUN npm run build

# Step 5: Nginx를 사용해 정적 파일 서빙
FROM nginx:1.21
COPY --from=build /app/build /usr/share/nginx/html

# Step 6: Expose port 80
EXPOSE 80

# Step 7: Nginx 실행
CMD ["nginx", "-g", "daemon off;"]
