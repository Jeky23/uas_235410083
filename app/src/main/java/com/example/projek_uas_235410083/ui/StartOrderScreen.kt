package com.example.projek_uas_235410083.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projek_uas_235410083.R
import com.example.projek_uas_235410083.data.DataSource

/** Composable yang memungkinkan pengguna untuk memilih jumlah kue yang diinginkan dan dipesan
 *[onNextButtonClicked] lambda yang memesan jumlah yang dipilih dan memicu navigasi ke layar berikutnya */
@Composable
fun StartOrderScreen(

    /** parameter quantityOptions yang memuat daftar Pair<Int, Int>.
    *  Pair adalah sepasang nilai, menggunakan dua parameter jenis generik yaitu int */
    quantityOptions: List<Pair<Int, Int>>,

    /**parameter bernama onNextButtonClicked dari jenis () -> Unit untuk pengendali button
    *  button dalam hal ini adalah 'Pesan satu', 'Pesan Sepuluh', dan 'Pesan Duapuluh'. */
    onNextButtonClicked: (Int) -> Unit,

    /** parameter modivier*/
    modifier: Modifier = Modifier
){

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        /** TODO : Card untuk membuat tampilan kartu dari gambar kue dan tombol button */
        Card(modifier = modifier) {

            /** Fungsi kolom yang memuat gambar, teks dan teksbutton */
            Column(

            ) {

                //fungsi spacer untuk memberi spasi anatara kolom dan gambar
                // dengan jarak 16 dp, jarak ini dipanggil dari file dimens.xml di paket value
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
                //fungsi teks untuk menampilkan teks 'Pesan Kue'
                Text(
                    text = stringResource(R.string.order_kue),
                    style = MaterialTheme.typography.headlineSmall
                )
                //fungsi image untuk menampilkan gambar yang diambil dari resource deawable dengan nama kue_putu
                Image(
                    painter = painterResource(R.drawable.kue_putu),
                    contentDescription = null,
                    modifier = Modifier.width(300.dp)
                )

                //fungsi spacer untuk memberi spasi anatara gambar dan teks Pesan Kue
                // dengan jarak 16 dp, jarak ini dipanggil dari file dimens.xml di paket value
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
                Row(modifier = Modifier.weight(1f, false)) {

                    // fungsi kolom yang didalamnya ada teksButton dengan jarak 16 dp
                    // jarak ini dipanggil dari file dimens.xml di paket value
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            dimensionResource(id = R.dimen.padding_medium)
                        )
                    ) {

                        /** parameter quantityOptions di composable StartOrderScreen,
                           Int pertama adalah ID resource untuk string yang ditampilkan pada setiap tombol.
                           Int kedua adalah jumlah kue sebenarnya.*/
                        quantityOptions.forEach { item ->
                            SelectQuantityButton(
                                labelResourceId = item.first,
                                onClick = { onNextButtonClicked(item.second) }// panggil onNextButtonClicked dengan meneruskan item.second, yaitu jumlah kue
                            )
                        }
                    }
                }

            }
        }
    }
}

/**Tombol yang dapat disesuaikan dapat disusun yang menampilkan [LabelResourceId]
 * dan memicu lambda [onClick] saat Composable ini diklik */

@Composable
fun SelectQuantityButton(
    @StringRes labelResourceId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Button(
        onClick = onClick,
        modifier = modifier.widthIn(min = 250.dp)
    ) {
        Text(stringResource(labelResourceId))
    }
}

@Preview
@Composable
fun StartOrderPreview(){
    StartOrderScreen(
        quantityOptions = DataSource.quantityOptions,
        onNextButtonClicked = {},
        modifier = Modifier.fillMaxSize().padding(dimensionResource(R.dimen.padding_medium))
    )
}
