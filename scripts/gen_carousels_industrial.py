# -*- coding: utf-8 -*-
"""Generate muted industrial carousel images (no neon / cyber glow)."""
from PIL import Image, ImageDraw
import os
import math
import random

pub = r"E:\new_code\20260819\frontend\public\images"
up = r"E:\new_code\20260819\uploads"
os.makedirs(pub, exist_ok=True)
os.makedirs(up, exist_ok=True)

W, H = 1600, 700


def lerp(a, b, t):
    return int(a + (b - a) * t)


def make_server_aisle(seed=1):
    random.seed(seed)
    img = Image.new("RGB", (W, H), (28, 32, 38))
    d = ImageDraw.Draw(img)

    # floor
    for y in range(H // 2, H):
        t = (y - H // 2) / (H // 2)
        c = (lerp(45, 22, t), lerp(48, 24, t), lerp(52, 28, t))
        d.line([(0, y), (W, y)], fill=c)

    # ceiling strip
    d.rectangle([0, 0, W, 70], fill=(18, 20, 24))

    # racks left and right vanishing to center
    cx = int(W * 0.62)
    for side in (-1, 1):
        for i in range(10):
            depth = i / 10
            x0 = cx + side * int(80 + depth * 520)
            top = int(90 + depth * 40)
            bot = int(H - 40 - depth * 20)
            w = max(28, int(90 - depth * 55))
            color = (lerp(55, 30, depth), lerp(58, 32, depth), lerp(62, 36, depth))
            d.rectangle([x0 - w, top, x0 + w, bot], fill=color)
            # subtle LED dots (dim blue, not neon)
            for row in range(8):
                for col in range(3):
                    lx = x0 - w + 10 + col * (w // 2)
                    ly = top + 18 + row * ((bot - top) // 9)
                    if 0 <= lx < W and 0 <= ly < H:
                        d.ellipse([lx, ly, lx + 3, ly + 3], fill=(70, 110, 150))

    # steel pipe (matte, no glow)
    for i, offset in enumerate([120, 150, 180]):
        pts = []
        for x in range(0, 520, 20):
            y = offset + int(18 * math.sin(x / 90.0 + seed))
            pts.append((x, y + 80))
        if len(pts) > 1:
            d.line(pts, fill=(90, 98, 105), width=10 - i)
            d.line(pts, fill=(120, 128, 134), width=3)

    # soft left vignette for text readability
    overlay = Image.new("RGBA", (W, H), (0, 0, 0, 0))
    od = ImageDraw.Draw(overlay)
    for x in range(0, 640):
        a = int(170 * (1 - x / 640))
        od.line([(x, 0), (x, H)], fill=(16, 24, 32, a))
    img = Image.alpha_composite(img.convert("RGBA"), overlay).convert("RGB")
    return img


def make_equipment(seed=2):
    random.seed(seed)
    img = Image.new("RGB", (W, H), (236, 238, 240))
    d = ImageDraw.Draw(img)
    # wall
    d.rectangle([0, 0, W, H], fill=(232, 234, 236))
    # equipment cabinet
    d.rectangle([720, 80, 1480, 620], fill=(48, 54, 62))
    d.rectangle([740, 100, 1460, 600], fill=(58, 64, 72))
    for y in range(130, 560, 48):
        d.rectangle([760, y, 1440, y + 28], fill=(42, 46, 52))
        d.rectangle([770, y + 8, 820, y + 18], fill=(90, 130, 160))
    # CDU-like unit
    d.rectangle([180, 200, 620, 560], fill=(210, 214, 218))
    d.rectangle([200, 220, 600, 300], fill=(0, 79, 184))
    d.rectangle([220, 340, 400, 520], fill=(180, 186, 192))
    d.ellipse([430, 360, 560, 490], outline=(120, 128, 136), width=8)
    d.ellipse([455, 385, 535, 465], fill=(160, 168, 176))
    # floor
    d.rectangle([0, 560, W, H], fill=(200, 204, 208))
    overlay = Image.new("RGBA", (W, H), (0, 0, 0, 0))
    od = ImageDraw.Draw(overlay)
    for x in range(0, 560):
        a = int(90 * (1 - x / 560))
        od.line([(x, 0), (x, H)], fill=(16, 24, 32, a))
    return Image.alpha_composite(img.convert("RGBA"), overlay).convert("RGB")


def make_plant(seed=3):
    random.seed(seed)
    img = Image.new("RGB", (W, H), (40, 44, 48))
    d = ImageDraw.Draw(img)
    d.rectangle([0, H - 120, W, H], fill=(55, 58, 62))
    # distant hall
    for i in range(12):
        x = 100 + i * 120
        d.rectangle([x, 120, x + 70, H - 120], fill=(35, 38, 42))
        for ly in range(150, H - 160, 40):
            d.rectangle([x + 12, ly, x + 58, ly + 8], fill=(65, 95, 120))
    # overhead cable trays (gray)
    for y in (90, 110):
        d.rectangle([0, y, W, y + 10], fill=(70, 74, 78))
    overlay = Image.new("RGBA", (W, H), (0, 0, 0, 0))
    od = ImageDraw.Draw(overlay)
    for x in range(0, 600):
        a = int(160 * (1 - x / 600))
        od.line([(x, 0), (x, H)], fill=(16, 24, 32, a))
    return Image.alpha_composite(img.convert("RGBA"), overlay).convert("RGB")


makers = [
    ("carousel-1.jpg", make_server_aisle, 1),
    ("carousel-2.jpg", make_equipment, 2),
    ("carousel-3.jpg", make_plant, 3),
]

for name, fn, seed in makers:
    im = fn(seed)
    for folder in (pub, up):
        path = os.path.join(folder, name)
        im.save(path, quality=90)
        print("saved", path)

print("done")
