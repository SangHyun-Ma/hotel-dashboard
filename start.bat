@echo off
chcp 65001 > nul
echo.
echo ================================
echo   Hotel Dashboard 시작
echo ================================
echo.

:: ── 1. Backend ──────────────────────────────────────────
echo [1/3] Backend 시작 (포트 8080)...
start "Hotel Backend" cmd /k "cd /d "%~dp0backend" && gradlew.bat bootRun"

:: Backend 기동 대기 (최대 60초)
echo [2/3] Backend 준비 대기 중...
set /a tries=0
:wait_backend
set /a tries+=1
if %tries% gtr 12 (
    echo.
    echo [오류] Backend가 60초 내에 시작되지 않았습니다.
    echo        Backend 창에서 오류 메시지를 확인하세요.
    pause
    exit /b 1
)
timeout /t 5 /nobreak > nul
curl -s -o nul -w "%%{http_code}" http://localhost:8080/api/dashboard/today-status 2>nul | findstr "200" > nul
if errorlevel 1 goto wait_backend
echo        Backend 준비 완료!

:: ── 2. Frontend ─────────────────────────────────────────
echo [3/3] Frontend 시작 (포트 3000)...
if not exist "%~dp0frontend\node_modules" (
    echo        node_modules 없음 - npm install 실행 중...
    cd /d "%~dp0frontend"
    call npm install
)
start "Hotel Frontend" cmd /k "cd /d "%~dp0frontend" && npm start"

:: ── 완료 ────────────────────────────────────────────────
echo.
echo ================================
echo   완료!
echo   http://localhost:3000 접속
echo ================================
echo.
echo 종료하려면 Backend/Frontend 창을 닫으세요.
pause
