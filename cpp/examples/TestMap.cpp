#include <iostream>
#include <string>
#include <map>

using namespace std;

int testMap()
{
	map<string, int> m;
	map<int, string> m2;
	for (int i = 65; i < 90; i++) {
		m[to_string(i)] = i;
		m2[i] = static_cast<char>(i);
	}
	for (int i = 65; i < 90; i++) {
		cout << "key: " << static_cast<char>(m[to_string(i)]) << "; value: " << m[to_string(i)] << endl;
		cout << "key: " << i << "; value: " << m2[i] << endl;
	}
	// Similar to above code
	for (auto iter = m.begin(); iter != m.end(); ++iter)
	{
		string k = iter->first;
		int v = iter->second;
		//cout << "key: " << static_cast<char>(v) << "; value: " << v << endl;
	}

	return 0;
}