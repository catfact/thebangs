~bang = Thebangs.new;

~bang.release = 2;

r = Routine {
	inf.do {
		arg i;
		var hz = 330 * ((i*7)%5 + 1)/((i*9)%7 + 1);
		var t = 0.2 * ((i*15)%5 + 1)/((i*11)%4 + 1);
		//[t, hz].postln;
		~bang.bang(hz);
		t.wait;
	};
}.play;

/*
r.stop;
*/

~bang.hz2 = 6000;
~bang.setWhichBang(4);
~bang.mod1=0.1;
~bang.mod2=2.6;

~bang.setWhichBang(3);
~bang.mod2=4.0;

q = Routine { inf.do {
	~bang.bang((30 + 60.rand).midicps);
	0.1.wait;
} }.play;

q.stop;


~bang.setWhichBang(3);

~bang.mod1=0.1;

~bang.voicer.v.size