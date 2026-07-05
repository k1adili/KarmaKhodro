# پوشه فونت Vazirmatn

این پوشه عمداً خالی گذاشته شده تا خودتان فایل‌های فونت Vazirmatn را در آن قرار دهید.

## مراحل:

1. به آدرس رسمی فونت مراجعه کنید:
   https://github.com/rastikerdar/vazirmatn/releases

2. از داخل فایل فشرده دانلود شده، پوشه `fonts/ttf` را پیدا کنید.

3. سه فایل زیر را انتخاب و **دقیقاً با همین نام‌ها** (حروف کوچک، بدون فاصله)
   در همین پوشه (app/src/main/res/font/) کپی کنید:

   - `vazirmatn_regular.ttf`   (از فایل Vazirmatn-Regular.ttf)
   - `vazirmatn_medium.ttf`    (از فایل Vazirmatn-Medium.ttf)
   - `vazirmatn_bold.ttf`      (از فایل Vazirmatn-Bold.ttf)

   نکته: نام فایل منبع اندروید (resource name) فقط می‌تواند شامل حروف
   کوچک انگلیسی، عدد و آندرلاین باشد؛ به همین دلیل باید نام فایل‌ها را
   دقیقاً به شکل بالا تغییر نام دهید.

4. این README.md را می‌توانید نگه دارید یا حذف کنید؛ روی بیلد تاثیری ندارد.

⚠️ توجه: تا وقتی این سه فایل .ttf را اضافه نکرده‌اید، بیلد پروژه
(چه در Android Studio و چه در GitHub Actions) با خطای
`unresolved reference: vazirmatn_regular` متوقف می‌شود، چون کد برنامه
(`ui/theme/Type.kt`) به‌طور مستقیم و قطعی از این فونت‌ها استفاده می‌کند.
