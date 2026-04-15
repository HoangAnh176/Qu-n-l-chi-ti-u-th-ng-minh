import subprocess

result = subprocess.run([r'D:\Dowload\quan-ly-chi-tieu-main\quan-ly-chi-tieu-main\gradlew.bat', 'assembleDebug', '--console=plain'], cwd=r'D:\Dowload\quan-ly-chi-tieu-main\quan-ly-chi-tieu-main', capture_output=True, text=True)

with open(r'D:\Dowload\quan-ly-chi-tieu-main\quan-ly-chi-tieu-main\mylog.txt', 'w', encoding='utf-8') as f:
    f.write(result.stdout)
    f.write('\n\n--- STDERR ---\n\n')
    f.write(result.stderr)

