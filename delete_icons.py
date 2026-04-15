import os
import sys
import glob

workspace = sys.argv[1]
target_dir = os.path.join(workspace, 'app', 'src', 'main', 'res', 'drawable')

deleted_count = 0
for prefix in ['ic_dm_', 'ic_new_', 'ic_cate_']:
    for ext in ['*.xml', '*.png']:
        pattern = os.path.join(target_dir, prefix + ext)
        files = glob.glob(pattern)
        for f in files:
            os.remove(f)
            deleted_count += 1

print(f"Deleted {deleted_count} files.")

