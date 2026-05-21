Write-Host ""
Write-Host " Hotel Dashboard - Shutdown"
Write-Host " =========================="
Write-Host ""

foreach ($port in @(8080, 3000)) {
    $line = netstat -ano | Select-String "TCP.*:$port .*LISTENING" | Select-Object -First 1
    if ($line) {
        $procId = ($line.ToString().Trim() -split '\s+')[-1]
        taskkill /PID $procId /F | Out-Null
        Write-Host " [OK] Port $port (PID $procId) stopped"
    } else {
        Write-Host " [--] Port $port already stopped"
    }
}

Write-Host ""
Write-Host " Done."
Write-Host ""
