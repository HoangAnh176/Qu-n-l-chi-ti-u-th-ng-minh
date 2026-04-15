import urllib.request, os

urls = [
    ('ic_outline_savings', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/action/savings/materialiconsoutlined/24px.svg'),
    ('ic_outline_monetization_on', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/editor/monetization_on/materialiconsoutlined/24px.svg'),
    ('ic_outline_account_balance', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/action/account_balance/materialiconsoutlined/24px.svg'),
    ('ic_outline_account_balance_wallet', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/action/account_balance_wallet/materialiconsoutlined/24px.svg'),
    ('ic_outline_attach_money', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/editor/attach_money/materialiconsoutlined/24px.svg'),
    ('ic_outline_paid', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/action/paid/materialiconsoutlined/24px.svg'),
    ('ic_outline_price_check', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/action/price_check/materialiconsoutlined/24px.svg'),
    ('ic_outline_request_quote', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/editor/request_quote/materialiconsoutlined/24px.svg'),
    ('ic_outline_sell', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/action/sell/materialiconsoutlined/24px.svg'),
    ('ic_outline_trending_up', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/action/trending_up/materialiconsoutlined/24px.svg'),
    ('ic_outline_local_atm', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/maps/local_atm/materialiconsoutlined/24px.svg'),
    ('ic_outline_storefront', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/action/storefront/materialiconsoutlined/24px.svg'),
    ('ic_outline_card_giftcard', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/action/card_giftcard/materialiconsoutlined/24px.svg'),
    ('ic_outline_emoji_events', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/social/emoji_events/materialiconsoutlined/24px.svg'),
    ('ic_outline_business_center', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/places/business_center/materialiconsoutlined/24px.svg'),
    ('ic_outline_real_estate_agent', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/social/real_estate_agent/materialiconsoutlined/24px.svg'),
    ('ic_outline_add_card', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/action/add_card/materialiconsoutlined/24px.svg'),
    ('ic_outline_credit_score', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/action/credit_score/materialiconsoutlined/24px.svg'),
    ('ic_outline_point_of_sale', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/action/point_of_sale/materialiconsoutlined/24px.svg'),
    ('ic_outline_receipt_long', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/action/receipt_long/materialiconsoutlined/24px.svg'),
    ('ic_outline_currency_exchange', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/action/currency_exchange/materialiconsoutlined/24px.svg'),
    ('ic_outline_toll', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/action/toll/materialiconsoutlined/24px.svg'),
    ('ic_outline_diamond', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/social/diamond/materialiconsoutlined/24px.svg'),
    ('ic_outline_stars', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/action/stars/materialiconsoutlined/24px.svg'),
    ('ic_outline_thumb_up', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/action/thumb_up/materialiconsoutlined/24px.svg'),
    ('ic_outline_handshake', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/social/handshake/materialiconsoutlined/24px.svg'),
    ('ic_outline_volunteer_activism', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/action/volunteer_activism/materialiconsoutlined/24px.svg'),
    ('ic_outline_money', 'https://raw.githubusercontent.com/google/material-design-icons/master/src/editor/money/materialiconsoutlined/24px.svg')
]

res_dir = r"D:\Dowload\quan-ly-chi-tieu-main\quan-ly-chi-tieu-main\app\src\main\res\drawable"

for name, url in urls:
    try:
        req = urllib.request.Request(url)
        with urllib.request.urlopen(req) as response:
            svg = response.read().decode('utf-8')
            start = svg.find('d=\"') + 3
            end = svg.find('\"', start)
            p = svg[start:end]
            if len(p) < 10: p = 'M12,2L12,2Z'

            xml_content = f"""<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24"
    android:tint="#000000">
  <path android:fillColor="#000000" android:pathData="{p}"/>
</vector>"""
            with open(os.path.join(res_dir, name + ".xml"), "w", encoding="utf-8") as f:
                f.write(xml_content)
    except Exception as e:
        print(f"Failed {name}: {e}")

# also build building and pie_chart because I used them!
extra = {
    "ic_outline_building": "M17,11V3H7v4H3v14h8v-4h2v4h8V11H17zM7,19H5v-2h2V19zM7,15H5v-2h2V15zM7,11H5V9h2V11zM11,15H9v-2h2V15zM11,11H9V9h2V11zM11,7H9V5h2V7zM15,15h-2v-2h2V15zM15,11h-2V9h2V11zM15,7h-2V5h2V7zM19,19h-2v-2h2V19zM19,15h-2v-2h2V15z",
    "ic_outline_pie_chart": "M11,2v20c-5.07,-0.5 -9,-4.79 -9,-10s3.93,-9.5 9,-10zM13,2.03v8.99h9C21.43,6.34 17.73,2.5 13,2.03zM13,12.98V22c4.74,-0.48 8.44,-4.34 8.97,-8.98L13,12.98z"
}
for name, p in extra.items():
    xml_content = f"""<vector xmlns:android="http://schemas.android.com/apk/res/android" android:width="24dp" android:height="24dp" android:viewportWidth="24" android:viewportHeight="24" android:tint="#000000"><path android:fillColor="#000000" android:pathData="{p}"/></vector>"""
    with open(os.path.join(res_dir, name + ".xml"), "w", encoding="utf-8") as f:
        f.write(xml_content)

print("Done generating income icons")

