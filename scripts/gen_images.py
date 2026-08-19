# -*- coding: utf-8 -*-
from PIL import Image, ImageDraw
import os

pub = r"E:\new_code\20260819\frontend\public\images"
up = r"E:\new_code\20260819\uploads"
os.makedirs(pub, exist_ok=True)
os.makedirs(up, exist_ok=True)

bases = []
for name in ["carousel-1.jpg", "carousel-2.jpg", "carousel-3.jpg", "login-bg.jpg"]:
    p = os.path.join(up, name)
    if os.path.exists(p):
        bases.append(p)

items = [
    ("product-1.jpg", "液冷服务器 LC-Server", (11, 94, 215)),
    ("product-2.jpg", "CDU 冷量分配单元", (0, 90, 160)),
    ("product-3.jpg", "高效冷却液 Coolant", (0, 120, 180)),
    ("product-4.jpg", "液冷机柜系统", (20, 60, 140)),
    ("product-5.jpg", "散热模组 Cold Plate", (30, 100, 170)),
    ("product-default.jpg", "LIQUICOOL 产品", (11, 94, 215)),
    ("tech-1.jpg", "浸没式液冷技术", (10, 40, 90)),
    ("tech-2.jpg", "冷板式液冷技术", (15, 70, 130)),
    ("tech-3.jpg", "智能温控系统", (20, 90, 150)),
    ("tech-4.jpg", "PUE优化算法", (25, 80, 140)),
    ("honor-1.jpg", "高新技术企业", (180, 140, 40)),
    ("honor-2.jpg", "绿色数据中心奖", (40, 140, 90)),
    ("honor-3.jpg", "创新科技奖", (160, 90, 40)),
    ("honor-default.jpg", "荣誉奖项", (180, 140, 40)),
    ("case-1.jpg", "智算中心液冷改造", (30, 60, 120)),
    ("case-2.jpg", "金融数据中心项目", (40, 70, 130)),
    ("case-3.jpg", "运营商机房节能", (50, 80, 140)),
    ("case-default.jpg", "成功案例", (30, 60, 120)),
    ("news-1.jpg", "液冷技术白皮书发布", (20, 80, 150)),
    ("news-2.jpg", "立冷科技新品发布会", (25, 90, 160)),
    ("news-3.jpg", "绿色算力高峰论坛", (35, 100, 170)),
    ("news-default.jpg", "新闻资讯", (20, 80, 150)),
    ("avatar-default.png", "USER", (11, 94, 215)),
    ("avatar-admin.png", "ADMIN", (180, 60, 50)),
    ("avatar-user1.png", "U1", (40, 140, 120)),
    ("logo.png", "LIQUICOOL", (11, 94, 215)),
]


def make_avatar(path, color):
    size = (256, 256)
    img = Image.new("RGB", size, color)
    d = ImageDraw.Draw(img)
    d.ellipse([20, 20, 236, 236], fill=(255, 255, 255))
    d.ellipse([78, 60, 178, 160], fill=color)
    d.ellipse([48, 140, 208, 260], fill=color)
    img.save(path)


def make(path, title, color, size=(1200, 800)):
    if bases:
        base = Image.open(bases[hash(title) % len(bases)]).convert("RGB").resize(size)
        overlay = Image.new("RGBA", size, (*color, 150))
        img = Image.alpha_composite(base.convert("RGBA"), overlay).convert("RGB")
    else:
        img = Image.new("RGB", size, color)
    d = ImageDraw.Draw(img)
    d.rectangle([0, 0, size[0], 90], fill=(10, 22, 40))
    d.text((40, 28), "LIQUICOOL LiLeng Tech", fill=(255, 255, 255))
    tw = min(900, size[0] - 80)
    box = [(size[0] - tw) // 2, size[1] // 2 - 60, (size[0] + tw) // 2, size[1] // 2 + 60]
    d.rectangle(box, fill=(255, 255, 255))
    d.rectangle(box, outline=color, width=4)
    d.text((box[0] + 30, box[1] + 35), title, fill=(10, 22, 40))
    d.text((40, size[1] - 50), "Data Center Liquid Cooling", fill=(255, 255, 255))
    img.save(path, quality=88)


for name, title, color in items:
    for folder in (pub, up):
        path = os.path.join(folder, name)
        if name.startswith("avatar"):
            make_avatar(path, color)
        elif name == "logo.png":
            img = Image.new("RGB", (320, 120), (255, 255, 255))
            d = ImageDraw.Draw(img)
            d.rectangle([0, 0, 320, 120], outline=color, width=4)
            d.text((30, 45), "LIQUICOOL", fill=color)
            img.save(path)
        else:
            make(path, title, color)
    print("ok", name)

print("public", len(os.listdir(pub)))
print("uploads", len(os.listdir(up)))
