void A() {};
void B() {};
void C() {};
void D() {};

void scope1() {
  A(); B(); C(); D();
}

void scope2() {
  A(); C(); D();
}

void scope3() {
  A(); B(); B();
}

void scope4() {
  B(); D(); scope1();
}

void scope5() {
  B(); D(); A();
}

void scope6() {
  B(); D();
}

int main() {
  scope1();
}

//clang -O0 -emit-llvm test1.cpp -c -o test1.bc
//opt -print-callgraph test1.bc 1>/dev/null 2>call_graph
