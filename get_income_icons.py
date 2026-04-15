import urllib.request, json
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

paths = []
for name, url in urls:
    try:
        req = urllib.request.Request(url)
        with urllib.request.urlopen(req) as response:
            svg = response.read().decode('utf-8')
            start = svg.find('d=\"') + 3
            end = svg.find('\"', start)
            p = svg[start:end]
            if len(p) < 10: p = 'M12,2L12,2Z'
            paths.append('"' + name + '": "' + p + '"')
    except Exception as e:
        paths.append('"' + name + '": "M12,2L12,2Z"')

print("[\n" + ",\n".join(paths) + "\n]")

