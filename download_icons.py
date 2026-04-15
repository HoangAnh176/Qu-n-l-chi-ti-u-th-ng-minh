import urllib.request
import os

names = [
    'account_balance', 'savings', 'monetization_on', 'paid', 'attach_money',
    'trending_up', 'currency_exchange', 'local_atm', 'business_center',
    'add_card', 'real_estate_agent', 'handshake', 'stars', 'emoji_events',
    'card_giftcard', 'wallet'
]

res_dir = r'D:\Dowload\quan-ly-chi-tieu-main\quan-ly-chi-tieu-main\app\src\main\res\drawable'

for name in names:
    found = False
    for v in range(25, 0, -1):
        url = f'https://fonts.gstatic.com/s/i/materialiconsoutlined/{name}/v{v}/24px.svg'
        try:
            req = urllib.request.Request(url)
            with urllib.request.urlopen(req) as response:
                svg = response.read().decode('utf-8')

                # Extract paths
                import xml.etree.ElementTree as ET
                try:
                    root = ET.fromstring(svg)
                    paths = []
                    for elem in root.iter():
                        if elem.tag.endswith('path') and 'd' in elem.attrib:
                            d = elem.attrib['d']
                            if d and d != 'M0 0h24v24H0V0z' and d != 'M0 0h24v24H0z' and d != 'M0,0h24v24H0V0z':
                                paths.append(f'<path android:fillColor="#000000" android:pathData="{d}"/>')

                    if paths:
                        xml_content = f'''<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24"
    android:tint="#000000">
  {''.join(paths)}
</vector>'''
                        file_path = os.path.join(res_dir, f'ic_outline_{name}.xml')
                        with open(file_path, 'w', encoding='utf-8') as f:
                            f.write(xml_content)
                        print(f'Generated {name}')
                        found = True
                        break
                except Exception as e:
                    pass
        except:
            pass
    if not found:
        print(f'Failed {name}')
print('Done!')
