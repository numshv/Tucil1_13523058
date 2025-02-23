<div align="center">
<h1>IQ Puzzler Pro</h1>
<h2>(Tugas Kecil 1 IF2211 Strategi Algoritma)</h2>
</div>

## Table of Contents
- [About](#about)
- [Get Started](#get-started)
- [How To Use It](#how-to-use-it)
- [Author](#author)


## About
IQ Puzzler Pro adalah permainan puzzle di mana pemain harus mengisi papan dengan balok-balok beragam bentuk hingga tidak ada ruang kosong tersisa. Dalam permainan nyata, pemain mendapatkan satu set balok dengan bentuk unik dan harus menyusunnya di papan sesuai aturan. Beberapa versi permainan juga memungkinkan rotasi dan pencerminan balok untuk menemukan solusi yang tepat.

<div align="center">
  
![IQ Puzzler Pro](https://i.imgur.com/xtZsQai.png)

(Sumber: https://www.smartgamesusa.com)

</div>

Program ini meniru konsep permainan tersebut dengan menerima input berupa:

- Papan dengan ukuran dan bentuk tertentu.
- Kumpulan balok puzzle dengan berbagai bentuk yang bisa diputar atau dicerminkan sebelum ditempatkan.

Program akan menggunakan algoritma brute force untuk mencoba semua kemungkinan penyusunan balok hingga menemukan satu solusi yang memenuhi aturan permainan. Jika solusi ditemukan, program akan menampilkan hasilnya dengan visualisasi papan yang terisi. Jika tidak ada solusi yang mungkin, program akan memberi tahu pengguna bahwa tidak ada solusi dari kombinasi balok-balok tersebut.

## Get Started
- Pastikan Java Development Kit (JDK) telah terinstal. Jika belum, anda dapat menginstall JDK pada link berikut
  https://www.oracle.com/id/java/technologies/downloads/

- Download file .zip pada release terbaru atau clone repository ini secara keseluruhan

## How To Use It

### 1. Menjalankan Program
- Buka CLI yang tersedia pada perangkat anda (command prompt, shell, etc.)
- Arahkan ke direktori utama folder project
- Jalankan file JAR dengan perintah:
  ```bash
  java -jar app.jar
  ```

### 2. Input File
Program membutuhkan file input dengan ekstensi `.txt` yang harus ditempatkan dalam folder `test`. 

#### Format Input
Program menerima dua jenis format input:

##### Format Papan DEFAULT
```
N M P
DEFAULT
puzzle_1_shape
puzzle_2_shape
...
puzzle_P_shape
```

##### Format Papan CUSTOM
```
N M P
CUSTOM
board_shape_row_1
board_shape_row_2
...
board_shape_row_N
puzzle_1_shape
puzzle_2_shape
...
puzzle_P_shape
```

Keterangan:
- `N`: Jumlah baris papan
- `M`: Jumlah kolom papan
- `P`: Jumlah balok puzzle
- Setiap balok puzzle direpresentasikan dengan karakter alfabet
- Untuk papan CUSTOM:
  - Karakter 'X': Space yang dapat diisi
  - Karakter '.': Space yang tidak dapat diisi

### 3. Output dan Penyimpanan Hasil
- Jika program menemukan solusi, akan ditanyakan apakah hasil ingin disimpan ke file `.txt`
- Jika memilih untuk menyimpan:
  - Program akan meminta input nama file
  - Tidak perlu menambahkan ekstensi `.txt` pada nama file
  - File hasil akan tersimpan dalam folder `Solutions` yang berada di dalam folder `test`

## Author
Noumisyifa Nabila Nareswari
13523058 - K01
Teknik Informatika ITB
