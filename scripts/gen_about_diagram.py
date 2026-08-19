# -*- coding: utf-8 -*-
from PIL import Image, ImageDraw
import os

out_dirs = [
    r"E:\new_code\20260819\frontend\public\images",
    r"E:\new_code\20260819\uploads",
]

W, H = 1000, 700

for folder in out_dirs:
    os.makedirs(folder, exist_ok=True)
    path = os.path.join(folder, "about-diagram.jpg")
    img = Image.new("RGB", (W, H), (245, 248, 255))
    d = ImageDraw.Draw(img)

    # left rack box
    d.rounded_rectangle([40, 80, 420, 620], radius=16, fill=(20, 40, 90), outline=(11, 94, 215), width=3)
    d.text((70, 110), "Data Center", fill=(255, 255, 255))
    for i, y in enumerate(range(170, 520, 55)):
        d.rounded_rectangle([70, y, 390, y + 40], radius=6, fill=(40, 80, 150))
        d.rectangle([90, y + 12, 200, y + 28], fill=(80, 180, 255))
        d.rectangle([220, y + 12, 360, y + 28], fill=(30, 120, 220))

    # pipes
    d.line([(420, 250), 560, 180], fill=(80, 180, 255), width=8)
    d.line([(420, 420), 560, 480], fill=(220, 80, 80), width=8)
    d.text((470, 150), "Vapor", fill=(11, 94, 215))
    d.text((470, 490), "Liquid", fill=(200, 60, 60))

    # condenser
    d.rounded_rectangle([560, 120, 760, 280], radius=12, fill=(230, 240, 255), outline=(11, 94, 215), width=2)
    d.text((580, 170), "Condenser unit", fill=(11, 94, 215))
    d.ellipse([600, 200, 720, 250], outline=(11, 94, 215), width=3)

    # AIoT network
    d.rounded_rectangle([560, 340, 960, 620], radius=12, fill=(10, 30, 70))
    d.text((590, 360), "AIoT system", fill=(120, 200, 255))
    nodes = [(620, 450), (700, 420), (780, 470), (860, 430), (700, 540), (820, 540)]
    for i, (x1, y1) in enumerate(nodes):
        for j, (x2, y2) in enumerate(nodes):
            if i < j:
                d.line([(x1, y1), (x2, y2)], fill=(40, 100, 180), width=1)
    for x, y in nodes:
        d.ellipse([x - 10, y - 10, x + 10, y + 10], fill=(80, 180, 255))

    d.text((40, 30), "LIQUICOOL Liquid Cooling + AIoT", fill=(11, 94, 215))
    img.save(path, quality=90)
    print("wrote", path)
