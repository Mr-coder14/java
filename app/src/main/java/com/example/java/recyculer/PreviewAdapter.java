package com.example.java.recyculer;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.PDFDetails;
import com.example.java.R;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.ViewHolder> {

    private Uri[] uris;
    private String[] fileNames;
    private String orderid;
    private float spiralCost = 20.0f;
    private Context context;




    private Activity activity;

    private ArrayList<PDFDetails> pdfDetailsList;



    public PreviewAdapter(Activity activity,Uri[] uris, String[] fileNames) {

        this.activity=activity;
        this.uris =  uris ;
        this.fileNames = fileNames ;
        this.orderid = generateOrderId();

        this.pdfDetailsList = new ArrayList<>();


        for (int i = 0; i < uris.length; i++) {
            pdfDetailsList.add(new PDFDetails());
        }
    }




    public  String getOrderid(){
        return orderid;
    }
    public ArrayList<PDFDetails> getPdfDetailsList(){
        return new ArrayList<>(pdfDetailsList);
    }


    private String generateOrderId() {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString().replaceAll("-", "");
        return uuidString.substring(0, Math.min(uuidString.length(), 10));
    }
    public float calculateTotalAmount() {
        float total = 0;
        for (PDFDetails details : pdfDetailsList) {
            total += Float.parseFloat(details.getFinalmat());
        }
        return total;
    }



    @NonNull
    @Override
    public PreviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.orderpreviewactivitytemplate, parent, false);
        return new ViewHolder(view,activity);

    }

    @Override
    public void onBindViewHolder(@NonNull PreviewAdapter.ViewHolder holder,  int position) {
        int p=position;
        holder.currentPdfIndex=position;
        holder.bind(uris[p], fileNames[p],p);

        PDFDetails pdfDetails = pdfDetailsList.get(p);

        holder.black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.colortxt.setText("Black");
                holder.Color="Black";
                pdfDetailsList.get(p).setColor("Black");
                holder.updateamt(p);
            }
        });



        holder.gradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.colortxt.setText("Gradient");
                holder.Color="Gradient";
                pdfDetailsList.get(p).setColor("Gradient");
                holder.updateamt(p);
            }
        });

        ArrayList<String> sheets=new ArrayList<>();
        sheets.add("A4");
        sheets.add("OHP");

        ArrayAdapter<String> adapter3=new ArrayAdapter<>(context,R.layout.spinner_item_layout,sheets);
        holder.spinner3.setAdapter(adapter3);

        holder.spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                holder.sheet=parent.getItemAtPosition(position1).toString();
                String a=parent.getItemAtPosition(position1).toString();
                pdfDetailsList.get(p).setSheet(a);
                holder.updateamt(p);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayList<String> format = new ArrayList<>();
        format.add("Front & Back");
        format.add("Front Only");
        ArrayAdapter<String> Adaptor1 = new ArrayAdapter<>(
                context,
                R.layout.spinner_item_layout,
                format
        );
        Adaptor1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(Adaptor1);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position2, long id) {
                holder.formats = parent.getItemAtPosition(position2).toString();
                String a =parent.getItemAtPosition(position2).toString();
                pdfDetailsList.get(p).setFormats(a);
                holder.updateamt(p);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayList<String> ratio = new ArrayList<>();
        ratio.add("1:1");
        ratio.add("1:2");
        ratio.add("1:4");
        ArrayAdapter<String> Adaptor2 = new ArrayAdapter<>(
                context,
                R.layout.spinner_item_layout,
                ratio
        );
        Adaptor2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner1.setAdapter(Adaptor2);
        holder.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position3, long id) {
                holder.ratios=parent.getItemAtPosition(position3).toString();
                String a=parent.getItemAtPosition(position3).toString();
                pdfDetailsList.get(p).setRatios(a);
                holder.updateamt(p);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.qty.setFilters(new InputFilter[]{new PreviewAdapter.InputFilterMinMax(1, 10000)});
        holder.qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = s.toString();
                if (!a.isEmpty()) {
                    holder.count = Integer.parseInt(a);
                    pdfDetailsList.get(p).setCount(Integer.parseInt(a));
                    holder.qtytxt1.setText(a);
                    holder.qtyno.setText(a);

                    holder.updateamt(p);
                } else {

                    holder.count = 1;
                    pdfDetailsList.get(p).setCount(Integer.parseInt("1"));

                    holder.qtytxt1.setText(String.valueOf(holder.count));
                    holder.qtyno.setText(String.valueOf(holder.count));


                }
            }
        });


        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.count <= 1) {
                    holder.count = 1;
                    pdfDetailsList.get(p).setCount(Integer.parseInt("1"));

                } else {
                    holder.count--;
                    pdfDetailsList.get(p).setCount(holder.count);
                    holder.qty.setText(String.valueOf(holder.count));
                    holder.qtytxt1.setText(String.valueOf(holder.count));
                    holder.qtyno.setText(String.valueOf(holder.count));
                    holder.updateamt(p);
                }
            }
        });
        holder.spiralSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.updateamt(p);
            }
        });


        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.count++;
                pdfDetailsList.get(p).setCount(holder.count);
                holder.qty.setText(String.valueOf(holder.count));
                holder.qtytxt1.setText(String.valueOf(holder.count));
                holder.qtyno.setText(String.valueOf(holder.count));
                holder.updateamt(p);
            }
        });

        holder.preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uris[holder.currentPdfIndex] != null) {
                    String mimeType = context.getContentResolver().getType(uris[holder.currentPdfIndex]);
                    if (mimeType == null) {
                        String extension = MimeTypeMap.getFileExtensionFromUrl(uris[holder.currentPdfIndex].toString());
                        mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uris[holder.currentPdfIndex], mimeType);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        activity.startActivity(intent);
                    } else {
                        Toast.makeText(context, "No application available to view this file", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "File URI is not valid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return uris.length;

    }

    public void cancelPendingTasks() {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fileNameTextView;
        private ImageView pdfThumbnail;
        private Switch spiralSwitch;

        private TextView  pg, amt1, qtyno, qtytxt1, perpageamt, colortxt,finalamt;

        private EditText qty;
        private int currentPdfIndex = 0;
        private Spinner spinner, spinner1, spinner3;

        private int count = 1;
        private float perpage = 0.75f;
        private CircleImageView black, gradient;
        private Button preview;
        private String formats = "Front & Back", ratios = "1:1", sheet = "A4", Color = "Black";
        private int pgsam;
        private ImageButton plus, minus;
        private Uri pdf;
        private float currentProgress = 0, finalamount;
        private float delivercharge = 10.0f, amtperqty;
        private Activity activity;

        public ViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            this.activity=activity;

            fileNameTextView = itemView.findViewById(R.id.filenametxt1admin);
            qtytxt1 = itemView.findViewById(R.id.qtytxt1admin);
            qtyno = itemView.findViewById(R.id.qtynoadmin);
            fileNameTextView = itemView.findViewById(R.id.filenametxt1admin);
            black = itemView.findViewById(R.id.blackcolor);
            gradient = itemView.findViewById(R.id.gradientcolor);
            pg = itemView.findViewById(R.id.pagenoadmin);
            spinner3 = itemView.findViewById(R.id.spinner3admin);
            spiralSwitch=itemView.findViewById(R.id.spiralswitch);
            spinner= itemView.findViewById(R.id.spinneradmin);
            amt1 = itemView.findViewById(R.id.amt1admin);
            spinner1 = itemView.findViewById(R.id.spinner1admin);
            finalamt = itemView.findViewById(R.id.finaladmin12);
            qty = itemView.findViewById(R.id.qtytxtadmin);
            preview = itemView.findViewById(R.id.preview);
            perpageamt = itemView.findViewById(R.id.perpageamtadmin);
            plus = itemView.findViewById(R.id.addqtyadmin);
            qtyno = itemView.findViewById(R.id.qtynoadmin);
            minus = itemView.findViewById(R.id.minusqtyadmin);
            pdfThumbnail = itemView.findViewById(R.id.pdfViewadmin);
            colortxt = itemView.findViewById(R.id.colorfontadmin);



        }

        private void updateamt(int position) {
            if(pdfDetailsList.get(position).getSheet().toString().equals("A4")){
                try {
                    String amt1Text = amt1.getText().toString().replaceAll("[^\\d.]", "");

                    if(pdfDetailsList.get(position).getColor().toString().equals("Gradient")){
                        perpage=10.0f;
                    }
                    else {
                        switch (pdfDetailsList.get(position).getRatios()) {
                            case "1:1":
                                perpage = 0.75f;
                                pdfDetailsList.get(position).setPerpage(String.valueOf(0.75));
                                break;
                            case "1:2":
                            case "1:4":
                                perpage = 0.85f;
                                pdfDetailsList.get(position).setPerpage(String.valueOf(0.85));
                                break;
                        }

                    }

                    amtperqty = perpage * pgsam;
                    finalamount = perpage * pgsam * count;
                    pdfDetailsList.get(position).setPerqtyamt(String.valueOf(amtperqty));






                    if (ratios.equals("1:1") && formats.equals("Front Only") && pgsam > 50) {
                        float discount = 0.05f * finalamount;
                        finalamount -= discount;
                    }

                    if((ratios.equals("1:2") || ratios.equals("1:4")) && formats.equals("Front & Back") && pgsam>200){
                        float discount = 0.05f * finalamount;
                        finalamount -= discount;
                    }
                    if (spiralSwitch.isChecked()) {
                        finalamount += spiralCost;
                    }



                    setamt();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }else if (pdfDetailsList.get(position).getSheet().toString().equals("OHP")) {
                String amt1Text = amt1.getText().toString().replaceAll("[^\\d.]", "");
                perpage=15.0f;
                pdfDetailsList.get(position).setPerpage(String.valueOf(15.0));
                amtperqty = perpage * pgsam;
                finalamount = perpage * pgsam * count;
                pdfDetailsList.get(position).setPerqtyamt(String.valueOf(amtperqty));
                if (spiralSwitch.isChecked()) {
                    finalamount += spiralCost;
                }
                setamt();


            }


        }

        private void setamt() {

            perpageamt.setText(String.format("%.2f", perpage));
            pdfDetailsList.get(currentPdfIndex).setPerpage(String.valueOf(perpage));
            pdfDetailsList.get(currentPdfIndex).setSpiral(spiralSwitch.isChecked());
            finalamt.setText(String.format("₹ %.2f", finalamount));
            amt1.setText(String.format("₹ %.2f", amtperqty));
            pdfDetailsList.get(currentPdfIndex).setPerqtyamt(String.valueOf(amtperqty));
            pdfDetailsList.get(currentPdfIndex).setFinalmat(String.valueOf(finalamount));

        }





        public void bind(Uri uri, String fileName,int position) {
            fileNameTextView.setText(fileName);
            loadFileInfo(uri, fileName, position);
            currentPdfIndex = position;
        }

        private void loadFileInfo(Uri uri, String fileName, int position) {
            String mimeType = context.getContentResolver().getType(uri);
            if (mimeType == null) {
                String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
            }
            if (mimeType != null) {
                if (mimeType.startsWith("application/pdf")) {
                    loadPdfInfo(uri, fileName, position);
                } else if (mimeType.startsWith("image/")) {
                    loadImageInfo(uri, fileName, position);
                } else if (mimeType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
                        mimeType.equals("application/vnd.ms-excel")) {
                    loadExcelInfo(uri, fileName, position);
                } else if (mimeType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation") ||
                        mimeType.equals("application/vnd.ms-powerpoint")) {
                    loadPowerPointInfo(uri, fileName, position);
                } else if (mimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                        mimeType.equals("application/msword")) {
                    loadWordInfo(uri, fileName, position);
                } else {
                    Toast.makeText(context, "Unsupported file type", Toast.LENGTH_SHORT).show();
                    activity.finish();
                }
            } else {
                Toast.makeText(context, "Unsupported file type", Toast.LENGTH_SHORT).show();
            }
        }

        private void loadWordInfo(Uri uri, String fileName, int position) {
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                int pageCount = 1;

                try {

                    XWPFDocument document = new XWPFDocument(inputStream);
                    pageCount = document.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
                    document.close();
                } catch (org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException e) {
                    inputStream.close();
                    inputStream = context.getContentResolver().openInputStream(uri);
                    long fileSize = inputStream.available();

                    pageCount = (int) (fileSize / 3000) + 1;
                }

                inputStream.close();

                pg.setText(String.valueOf(pageCount));
                pgsam = pageCount;
                pdfDetailsList.get(position).setPages(String.valueOf(pageCount));

                pdfThumbnail.setImageResource(R.drawable.pngegg);

                updateamt(position);
                float a = pgsam * perpage ;
                perpageamt.setText(String.valueOf(perpage));
                amt1.setText("₹ " + a);
                amtperqty = a;
                finalamt.setText("₹ " + a);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error loading Word file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        private void loadPowerPointInfo(Uri uri, String fileName, int position) {
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                XMLSlideShow ppt = new XMLSlideShow(inputStream);
                int slidesCount = ppt.getSlides().size();

                pg.setText(String.valueOf(slidesCount));
                pgsam = slidesCount;
                pdfDetailsList.get(position).setPages(String.valueOf(slidesCount));
                
                pdfThumbnail.setImageResource(R.drawable.pngegg);

                ppt.close();
                inputStream.close();

                updateamt(position);
                float a = pgsam * perpage;
                perpageamt.setText(String.valueOf(perpage));
                amt1.setText("₹ " + a);
                amtperqty = a;
                finalamt.setText("₹ " + a);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error loading PowerPoint file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        private void loadExcelInfo(Uri uri, String fileName, int position) {
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                Workbook workbook = WorkbookFactory.create(inputStream);
                int sheetsCount = workbook.getNumberOfSheets();

                pg.setText(String.valueOf(sheetsCount));
                pgsam = sheetsCount;
                pdfDetailsList.get(position).setPages(String.valueOf(sheetsCount));


                pdfThumbnail.setImageResource(R.drawable.pngegg);

                workbook.close();
                inputStream.close();

                updateamt(position);
                float a = pgsam * perpage ;
                perpageamt.setText(String.valueOf(perpage));
                amt1.setText("₹ " + a);
                amtperqty = a;
                finalamt.setText("₹ " + a);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error loading Excel file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        private void loadImageInfo(Uri uri, String fileName, int position) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                pdfThumbnail.setImageBitmap(bitmap);
                pg.setText("1");
                pgsam = 1;
                pdfDetailsList.get(position).setPages("1");
                updateamt(position);
                float a = pgsam * perpage ;
                perpageamt.setText(String.valueOf(perpage));
                amt1.setText("₹ " + a);
                amtperqty = a;
                finalamt.setText("₹ " + a);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error loading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        private void loadPdfInfo(Uri uri, String fileName, int position) {
            try {
                ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
                PdfRenderer renderer = new PdfRenderer(parcelFileDescriptor);

                int pageCount = renderer.getPageCount();
                pg.setText(String.valueOf(pageCount));
                pgsam = pageCount;
                pdfDetailsList.get(position).setPages(String.valueOf(pgsam));


                PdfRenderer.Page page = renderer.openPage(0);
                Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                pdfThumbnail.setImageBitmap(bitmap);

                page.close();
                renderer.close();
                parcelFileDescriptor.close();

                updateamt(position);
                float a = pgsam * perpage ;
                perpageamt.setText(String.valueOf(perpage));
                amt1.setText("₹ " + a);
                amtperqty = a;
                finalamt.setText("₹ " + a);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error loading PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }



    }




    class InputFilterMinMax implements InputFilter {
        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c){
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}
