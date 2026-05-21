$root = $PSScriptRoot

Write-Host ""
Write-Host " Hotel Dashboard - Start"
Write-Host " ========================"
Write-Host ""

# ── 1. Backend ────────────────────────────────────────────
# -ArgumentList 은 wildcard 해석 없이 문자열 그대로 cmd 에 전달
Write-Host " [1/3] Starting Backend (port 8080)..."
Start-Process -FilePath "cmd.exe" -ArgumentList "/k `"$root\run-backend.bat`""

# ── 2. Backend 준비 대기 ──────────────────────────────────
Write-Host " [2/3] Waiting for Backend..."
$tries = 0
$ready = $false
while ($tries -lt 12 -and -not $ready) {
    Start-Sleep -Seconds 5
    $tries++
    try {
        Invoke-RestMethod "http://localhost:8080/api/dashboard/today-status" `
            -ErrorAction Stop | Out-Null
        $ready = $true
        Write-Host "       Backend ready! ($($tries * 5)s)"
    } catch {
        Write-Host "       Waiting... ($tries/12)"
    }
}

if (-not $ready) {
    Write-Host " [ERROR] Backend failed to start within 60 seconds."
    exit 1
}

# ── 3. Frontend ───────────────────────────────────────────
Write-Host " [3/3] Starting Frontend (port 3000)..."
$frontendDir = Join-Path $root "frontend"
if (-not (Test-Path -LiteralPath (Join-Path $frontendDir "node_modules"))) {
    Write-Host "       node_modules not found - running npm install..."
    & npm install --prefix $frontendDir
}
Start-Process -FilePath "cmd.exe" -ArgumentList "/k `"$root\run-frontend.bat`""

Write-Host ""
Write-Host " ========================"
Write-Host " Done!  http://localhost:3000"
Write-Host " ========================"
Write-Host ""
