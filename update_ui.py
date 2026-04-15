import os
import re

d = 'app/src/main/res/layout'
for f in os.listdir(d):
    if f.endswith('.xml'):
        filepath = os.path.join(d, f)
        with open(filepath, 'r', encoding='utf-8') as file:
            content = file.read()

        content = re.sub(r'android:background="@color/white"', 'android:background="?android:colorBackground"', content)
        content = re.sub(r'app:cardBackgroundColor="@color/white"', 'app:cardBackgroundColor="?attr/colorSurface"', content)
        content = re.sub(r'app:cardCornerRadius="[0-9]+dp"', 'app:cardCornerRadius="16dp"', content)
        content = re.sub(r'android:backgroundTint="@color/[a-zA-Z0-9_]+"', 'android:backgroundTint="?attr/colorPrimary"', content)
        content = re.sub(r'app:backgroundTint="@color/[a-zA-Z0-9_]+"', 'app:backgroundTint="?attr/colorPrimary"', content)

        with open(filepath, 'w', encoding='utf-8') as file:
            file.write(content)
print("Updated layouts")

