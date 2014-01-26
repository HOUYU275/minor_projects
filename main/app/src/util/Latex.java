package util;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 08/03/13
 * Time: 11:33
 */
public class Latex {

    private static String cfs = "arrhythmia\t0.031098637710252456\t279.0\t0.3485042567298752\t10.544\t0.3689326952379062\t15.512000000000006\t0.4669868106401823\t24.624000000000002\t0.393810880560672\t27.691999999999993\t0.2629804084032469\t61.40000000000002\t0.4658774239369692\t26.848\t0.27485497168298073\t58.367999999999995\t0.27993411633293674\t16.448\t0.46601892466681194\t23.496000000000002\t0.46711075463353546\t25.14\t\n" +
            "cleveland\t0.08639369072071965\t13.0\t0.27143788166887256\t5.36\t0.2391178066930793\t6.36\t0.27143788166887256\t5.36\t0.26876478211735205\t5.007999999999999\t0.273658434652813\t6.7\t0.273658434652813\t6.7\t0.273658434652813\t6.7\t0.27361332762021073\t6.68\t0.2663095255528135\t4.414000000000001\t0.27365780722094574\t6.62\t\n" +
            "ecoli\t0.41133255224315834\t7.0\t0.6453089230571646\t4.02\t0.6199946955514468\t5.02\t0.6453089230571646\t4.02\t0.6391063562294873\t3.7639999999999993\t0.64804699746126\t4.9\t0.64804699746126\t4.9\t0.64804699746126\t4.9\t0.64804699746126\t4.9\t0.6438154521799261\t3.9200000000000004\t0.64804699746126\t4.9\t\n" +
            "glass\t0.30386391336224355\t9.0\t0.5160059965590765\t5.34\t0.49866558716842874\t6.34\t0.5160059965590765\t5.34\t0.5073840635258825\t4.784000000000001\t0.5188298531639326\t6.62\t0.5187958343411908\t6.580000000000001\t0.5188298531639326\t6.62\t0.5188201931312059\t6.612\t0.5117568949325894\t4.86\t0.5185666631963701\t6.34\t\n" +
            "handwritten\t0.2859936765531236\t256.0\t0.4348800746314349\t91.99599999999998\t0.44398451151255536\t31.548\t0.5274400510493273\t78.01200000000003\t0.48375585709474855\t57.968\t0.513323345208309\t97.94\t0.5253198457749757\t93.74199999999999\t0.4674765440818403\t120.412\t0.4733476340598224\t122.262\t0.5262391452878282\t68.17599999999999\t0.5271368993257037\t82.26\t\n" +
            "heart\t0.12698794168152533\t13.0\t0.33651027633088204\t5.32\t0.3134606953910458\t6.32\t0.33651027633088204\t5.32\t0.3336904982661386\t5.048000000000001\t0.33834870210990375\t6.42\t0.3383453423648065\t6.412000000000001\t0.33834870210990375\t6.42\t0.3382927162398922\t6.402\t0.33273307731214097\t4.669999999999999\t0.33546555446698123\t5.34\t\n" +
            "ionosphere\t0.3148063179361768\t34.0\t0.5225933533547963\t9.608000000000002\t0.510927670812273\t9.54\t0.5383426297201825\t9.42\t0.5283857754521375\t9.244000000000002\t0.5389723055184119\t10.35\t0.5389756207997176\t10.33\t0.5389698386187122\t10.36\t0.5354638923988976\t10.943999999999999\t0.5358402441281737\t8.411999999999999\t0.5373469983523946\t9.3\t\n" +
            "libras\t0.24867244193275131\t90.0\t0.5769965427228526\t23.472\t0.570307054365511\t15.624\t0.6099386501146897\t22.984\t0.5891317002097748\t21.676\t0.6105332400682528\t31.374000000000002\t0.6113732879527233\t28.662\t0.6066285433063577\t33.715999999999994\t0.5951008086591192\t26.580000000000005\t0.6068165888203958\t19.08\t0.6107527350151785\t25.04\t\n" +
            "multifeat\t0.8947136152962482\t288.36\t0.8236454251041644\t235.29999999999995\t0.8017810771942419\t26.9\t0.9227044015286466\t106.208\t0.8355103627816277\t88.376\t0.8785788972103158\t258.226\t0.9188604365191559\t141.222\t0.8364561426890659\t317.98999999999995\t0.849038156229886\t356.85200000000003\t0.925607220843133\t75.992\t0.9261866927115366\t79.98\t\n" +
            "ozone\t0.061586490506248114\t72.0\t0.10041200895236962\t14.012000000000002\t0.10215289966212986\t10.696000000000003\t0.11195645699295033\t14.015999999999998\t0.1058129604447871\t13.032\t0.11410942834325005\t21.888\t0.11418097929465476\t21.376000000000005\t0.11346829501065145\t23.322\t0.11023328965864054\t25.296\t0.10714600909533202\t9.454000000000002\t0.1139883317836417\t19.82\t\n" +
            "secom\t0.002552217366093117\t257.0\t0.02387658539349805\t2.88\t0.08495531326177985\t14.231999999999996\t0.10141448570118766\t14.508000000000003\t0.04475640320277922\t15.7\t0.008190208167297965\t97.04999999999998\t0.06447854627799708\t22.476\t0.025119285312464337\t95.56200000000001\t0.017920685037150275\t20.81\t0.10107333560279169\t14.05\t0.1004872383218759\t14.26\t\n" +
            "sonar\t0.04161535321098042\t60.0\t0.330297427196596\t11.403999999999998\t0.31620833316940283\t17.728\t0.359682080974775\t16.708000000000002\t0.339495780266683\t12.511999999999999\t0.3600637142547084\t17.69\t0.36009946918406593\t17.656\t0.3598433833000509\t17.601999999999997\t0.3287830845587698\t11.876000000000001\t0.35767944004558416\t15.432\t0.3588010063088189\t16.56\t\n" +
            "water\t0.07314918576060161\t38.0\t0.4172531135775708\t8.196\t0.38666838504347334\t10.296\t0.4255241956986029\t9.388\t0.41592460021456723\t7.536000000000002\t0.4261608707430266\t10.492\t0.4261480568981454\t10.475999999999999\t0.4261373583797032\t10.462000000000003\t0.4172589528399374\t8.88\t0.4228909018869689\t8.352\t0.424086589786594\t9.16\t\n" +
            "waveform\t0.06606986009158218\t40.0\t0.3659221120582889\t11.340000000000005\t0.3608829024257968\t14.468\t0.3833696152456944\t13.12\t0.3636959548471328\t10.459999999999997\t0.38440857994037053\t14.92\t0.3844084569128276\t14.908\t0.38440857994037053\t14.92\t0.37155823422613304\t12.386\t0.3820957393900541\t12.606\t0.38395138942337115\t13.9\t\n";

    private static String pcfs = "arrhythmia\t0.9885935779039225\t273.88\t0.9865483351690248\t121.80799999999998\t0.7517947979327291\t8.8\t0.9882986407813995\t107.008\t0.977197927887583\t38.699999999999996\t0.9885935779039225\t45.63799999999999\t0.9885886517955481\t29.109999999999992\t0.9885935779039225\t113.84600000000002\t0.9885935779039225\t110.76799999999999\t0.9885739218842664\t21.07\t0.9830377264860023\t21.58\t\n" +
            "cleveland\t0.7812183464698979\t13.0\t0.7812183464698979\t7.968\t0.7751581977751693\t8.784\t0.7812183464698979\t7.94\t0.7151188998826093\t6.3759999999999994\t0.7812183464698979\t7.942\t0.7812183464698979\t7.94\t0.7812183464698979\t7.9479999999999995\t0.7812183464698979\t7.9479999999999995\t0.7812183464698979\t7.972000000000001\t0.7379017832187378\t6.68\t\n" +
            "ecoli\t0.9024505496907307\t7.0\t0.8994734771490392\t5.42\t0.9013523047669005\t6.42\t0.8994734771490392\t5.42\t0.8856505584333267\t4.6080000000000005\t0.9024505496907307\t5.98\t0.9024505496907307\t5.98\t0.9024505496907307\t5.98\t0.9024505496907307\t5.98\t0.8896437173518676\t4.561999999999999\t0.894051100474286\t4.98\t\n" +
            "glass\t0.8598089378238343\t9.0\t0.855344883419689\t5.9\t0.8568598877374783\t6.9\t0.855344883419689\t5.9\t0.8441102115716752\t5.3599999999999985\t0.8598089378238343\t6.76\t0.8598089378238343\t6.76\t0.8598089378238343\t6.76\t0.8598089378238343\t6.76\t0.8490630936960278\t5.3039999999999985\t0.8520191062176162\t5.76\t\n" +
            "handwritten\t1.0\t18.44\t0.9999748895578519\t27.092000000000002\t0.9999330407674837\t26.916\t1.0\t21.999999999999996\t1.0\t23.14\t1.0\t41.205999999999996\t1.0\t70.184\t1.0\t40.45399999999999\t1.0\t23.977999999999998\t1.0\t83.376\t0.9993862248786084\t18.06\t\n" +
            "heart\t0.9613991769547325\t13.0\t0.9613004115226339\t9.468\t0.9547325102880657\t10.172000000000004\t0.9613991769547325\t9.46\t0.9146666666666667\t7.212000000000001\t0.9613991769547325\t9.46\t0.9613991769547325\t9.46\t0.9613991769547325\t9.46\t0.9613991769547325\t9.46\t0.9221234567901238\t8.671999999999999\t0.9473251028806584\t8.34\t\n" +
            "ionosphere\t0.9958454106280198\t29.72\t0.9957101449275366\t9.828000000000001\t0.992599033816425\t9.960000000000003\t0.9958454106280198\t7.035999999999997\t0.9956135265700488\t8.675999999999998\t0.9958454106280198\t10.036\t0.9957487922705313\t6.686\t0.9958454106280198\t10.096000000000002\t0.9958454106280198\t8.096\t0.9889661835748795\t15.559999999999997\t0.9907246376811593\t6.36\t\n" +
            "libras\t0.9718518518518517\t90.0\t0.9705308641975313\t35.208\t0.9349259259259258\t18.959999999999994\t0.9715185185185184\t17.2\t0.9682962962962963\t24.519999999999996\t0.9718518518518517\t18.238000000000003\t0.9711913580246911\t15.922000000000002\t0.9718518518518517\t33.29599999999999\t0.9718518518518517\t28.393999999999995\t0.9607283950617282\t41.62400000000002\t0.9670370370370368\t16.04\t\n" +
            "multifeat\t1.0\t6.54\t1.0\t14.588\t1.0\t10.644\t1.0\t13.068000000000005\t1.0\t19.543999999999997\t1.0\t44.884000000000015\t0.9999822222222222\t9.099999999999996\t1.0\t43.843999999999994\t1.0\t13.408000000000001\t1.0\t325.17199999999997\t0.999688888888889\t6.12\t\n" +
            "ozone\t0.9996229031588177\t62.68\t0.9969850150364953\t22.951999999999998\t0.9689506695278309\t14.171999999999999\t0.9988441496112048\t16.648\t0.9957642615965605\t20.120000000000005\t0.9996229031588177\t20.985999999999994\t0.9996053631448197\t17.978\t0.9996229031588177\t31.828000000000003\t0.9996229031588177\t26.040000000000006\t0.9943059173031221\t34.526\t0.9988160971257607\t19.12\t\n" +
            "secom\t0.9895906730803062\t310.28\t0.9861246417459575\t210.688\t0.9356053581032515\t2.480000000000001\t0.989482905841137\t213.00800000000004\t0.9735828017954171\t150.0\t0.9895906730803062\t197.84799999999996\t0.9895850013319862\t26.462000000000003\t0.9875826107936123\t314.4119999999999\t0.9895906730803062\t256.08799999999997\t0.9713170258003229\t293.8960000000001\t0.9788401465687532\t97.28\t\n" +
            "sonar\t0.9930526794857206\t48.4\t0.9928176129252472\t24.787999999999997\t0.9462158379792922\t11.616000000000003\t0.9929458413926499\t11.655999999999999\t0.9891207190806688\t15.764000000000001\t0.9930526794857206\t12.412000000000003\t0.9925398225054045\t11.074000000000002\t0.9930314028899762\t23.688\t0.9930526794857206\t17.64\t0.9241712367732394\t30.336000000000002\t0.9850403913983391\t11.6\t\n" +
            "water\t0.9948148148148144\t32.72\t0.9936638176638175\t15.840000000000002\t0.9754301994301994\t10.764000000000001\t0.9948148148148144\t9.771999999999998\t0.9904045584045584\t10.492\t0.9948148148148144\t9.999999999999996\t0.9947692307692304\t10.708\t0.9948148148148144\t13.82\t0.9948148148148144\t12.151999999999997\t0.9274074074074073\t18.924\t0.9904273504273506\t9.3\t\n" +
            "waveform\t1.0\t11.54\t0.9985573333333335\t12.355999999999996\t0.9991262222222222\t11.36\t0.9994675555555556\t9.728\t0.9987173333333338\t10.852\t1.0\t11.455999999999996\t1.0\t11.112\t1.0\t14.920000000000005\t1.0\t12.908000000000005\t0.9789164444444443\t20.075999999999993\t0.9995955555555557\t10.56\t\n";

    private static String frfs = "arrhythmia\t1.0\t19.846153846153847\t0.9998290243873242\t40.35\t0.9999383792569938\t23.625\t0.9999315351648501\t28.95\t0.9999216949021745\t29.225\t1.0\t63.96153846153846\t1.0\t24.26923076923077\t1.0\t63.084615384615375\t1.0\t34.73846153846153\t1.0\t108.00769230769232\t0.9999969999653311\t19.807692307692307\t\n" +
            "cleveland\t0.9986667865840091\t12.8\t0.9288552844038265\t13.0\t0.9288552844038265\t13.0\t0.9288552844038265\t13.0\t0.8537008915707045\t10.85\t0.9986667865840091\t12.8\t0.9986667865840091\t12.808\t0.9986667865840091\t12.8\t0.9986667865840091\t12.8\t0.9444076233581895\t11.42\t0.9876698384194594\t11.8\t\n" +
            "ecoli\t0.6777503364368371\t6.92\t0.9426061450784253\t6.45\t0.9436553851606441\t6.95\t0.9426061450784253\t6.45\t0.9322382036254435\t5.9\t0.6777503364368371\t6.82\t0.6777503364368371\t6.82\t0.8870959170829736\t6.739130434782608\t0.6777503364368371\t6.82\t0.6453322357026207\t5.874\t0.6609713107746183\t5.82\t\n" +
            "glass\t0.9956195309218125\t8.86\t0.48822939153670103\t9.0\t0.48822939153670103\t9.0\t0.7402319139673083\t8.95\t0.4358784420769604\t7.475\t0.9956195309218125\t8.84\t0.9956195309218125\t8.84\t0.8523295885397505\t8.875\t0.9956195309218125\t8.84\t0.971973206902662\t8.468\t0.9626469461960844\t7.86\t\n" +
            "handwritten\t1.0\t19.85\t0.999947686578858\t26.625\t0.9997907706472556\t27.475\t1.0\t21.95\t1.0\t22.925\t1.0\t40.4\t0.9968073970690858\t22.475\t1.0\t40.025\t1.0\t23.775\t1.0\t128.85\t0.998535467526261\t19.0\t\n" +
            "heart\t0.8807090909277079\t7.84\t0.9591884766621599\t12.8\t0.9598729913033388\t13.0\t0.9591884766621599\t12.8\t0.9085563391101766\t10.7\t1.0\t10.612\t1.0\t10.584000000000001\t1.0\t10.611999999999998\t1.0\t10.816000000000004\t0.9854490149690333\t10.816\t0.974891701299094\t9.68\t\n" +
            "ionosphere\t1.0\t26.08\t0.9930766564009662\t15.25\t0.9940468146135268\t16.625\t0.9934151032608695\t13.975\t0.991878495772947\t15.075\t1.0\t24.519999999999996\t0.9999989629791295\t24.423999999999996\t0.9999995121838333\t25.02799999999999\t1.0\t25.424\t0.9914999198753719\t14.984000000000005\t0.9999472363587456\t24.9\t\n" +
            "libras\t1.0\t21.76923076923077\t0.9970459356402686\t19.7\t0.9972239415757006\t21.3\t0.9976475448781686\t18.725\t0.9971302583436532\t19.5\t1.0\t29.57692307692308\t1.0\t20.9\t1.0\t30.11538461538462\t1.0\t23.323076923076922\t0.9990942032315849\t26.223076923076924\t0.9995846614690992\t20.76923076923077\t\n" +
            "multifeat\t1.0\t13.533333333333333\t0.9999748961391558\t20.186666666666667\t0.9999930200380378\t17.857142857142858\t0.9999865144649561\t18.575\t0.9999705365332465\t26.592857142857145\t1.0\t49.82142857142857\t0.9996198047833571\t15.321428571428571\t1.0\t41.82142857142857\t1.0\t21.035714285714285\t1.0\t323.25\t0.5615675060672615\t6.0\t\n" +
            "ozone\t0.9820054737167799\t63.9\t0.9750489021516087\t36.45\t0.9622173193917212\t35.7\t0.9543388849440391\t31.75\t0.9756086313257704\t34.5\t0.9820054737167799\t48.5\t0.9241772533852904\t38.45\t0.9818578652839658\t48.75\t0.9820054737167799\t51.9\t0.9187842601448141\t36.1\t0.9787862235778817\t33.7\t\n" +
            "secom\t1.0\t15.25\t0.9999002155167334\t35.23076923076923\t0.9999695185373233\t20.458333333333332\t0.9999749798786896\t28.85\t0.999947642506759\t26.625\t1.0\t67.5\t0.9999893640739023\t15.75\t1.0\t67.08333333333333\t1.0\t31.666666666666668\t1.0\t296.7916666666667\t0.8027527198168937\t7.0\t\n" +
            "sonar\t1.0\t12.65\t0.9996549474310825\t13.325\t0.9998853219063882\t14.5\t0.9998085044340419\t11.9\t0.9997363482714736\t13.475\t1.0\t17.259999999999998\t1.0\t13.029999999999998\t1.0\t16.919999999999998\t1.0\t14.050000000000002\t0.9996650683543307\t17.779999999999998\t0.9990856281968925\t11.65\t\n" +
            "water\t1.0\t20.85\t0.9980978500616338\t17.975\t0.9981870554857586\t19.0\t0.9983767382134145\t15.85\t0.9971541752233758\t17.775\t1.0\t20.845\t0.9999977897552043\t19.82\t1.0\t22.014999999999997\t1.0\t22.09\t0.9813534519646053\t19.155\t0.9999030832826395\t19.85\t\n" +
            "waveform\t1.0\t18.4\t0.9995955648225877\t16.95\t0.9998180111396765\t18.0\t0.9988924548704524\t16.1\t0.9995085804971117\t17.0\t1.0\t19.15\t0.8764608348471723\t14.55\t1.0\t19.4\t1.0\t19.15\t0.996294394485485\t20.45\t0.9999082649788681\t17.4\t\n";

    public static void main(String[] args) {
        System.out.println(highlight(frfs));
    }

    public static String highlight(String input) {
        DecimalFormat format3 = new DecimalFormat("#.###");
        DecimalFormat format1 = new DecimalFormat("#.#");
        DecimalFormat format0 = new DecimalFormat("#");
        //Double.valueOf(twoDForm.format(d));
        String[] lines = input.split("\n");
        StringBuffer buffer = new StringBuffer();
        for (String line : lines) {
            String[] entries = line.split("\t");
            int size = (entries.length - 3) / 2;
            double[] merits = new double[size];
            double[] sizes = new double[size];
            ArrayList<Integer> highestIndices;
            for (int i = 3; i < entries.length - 1; i += 2) {
                merits[i / 2 - 1] = Double.valueOf(format3.format(Double.valueOf(entries[i])));
                sizes[i / 2 - 1] = Double.valueOf(format1.format(Double.valueOf(entries[i + 1])));
            }
            highestIndices = getHighest(merits, sizes);
            buffer.append(entries[0]).append(" & ");
            for (int i = 3; i < entries.length; i++) {
                double number = Double.parseDouble(entries[i]);
                if (i % 2 == 0) {
                    number = Double.valueOf(format1.format(number));
                    //if (number >= 100) number = Integer.valueOf(format0.format(number));
                } else number = Double.valueOf(format3.format(number));
                boolean set = false;
                for (int highestIndex : highestIndices) {
                    if (i == (3 + highestIndex * 2)) {
                        buffer.append("\\textbf{").append(number > 100 ? ((int)number) : number).append("}").append(" & ");
                        set = true;
                    }
                    //else buffer.append(number)s.append(" & ");
                }
                if (!set & number > 100)
                    buffer.append((int)number).append(" & ");
                else if (!set) buffer.append(number).append(" & ");
            }
            buffer.delete(buffer.length() - 3, buffer.length());
            buffer.append(" \\\\\n");
        }
        return buffer.toString();
    }

    public static ArrayList<Integer> getHighest(double[] merits, double[] sizes) {
        ArrayList<Integer> highestIndices = new ArrayList<>();
        double currentHighest = -Double.MAX_VALUE;
        for (double input1 : merits) {
            if (input1 > currentHighest) {
                currentHighest = input1;
            }
        }
        double currentSmallest = Double.MAX_VALUE;
        for (int i = 0; i < merits.length; i++) {
            if ((merits[i] == currentHighest) && sizes[i] <= currentSmallest) currentSmallest = sizes[i];
        }
        for (int i = 0; i < merits.length; i++) {
            if ((merits[i] == currentHighest) && sizes[i] == currentSmallest) highestIndices.add(i);
        }

        return highestIndices;
    }

}
